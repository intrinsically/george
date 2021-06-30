import antd.dropdown.dropdown
import antd.layout.content
import antd.menu.*
import antd.tooltip.*
import costanza.george.diagrams.Together
import costanza.george.diagrams.base.Diagram
import costanza.george.diagrams.drawingEntityTypes
import costanza.george.geometry.Coord
import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.reflect.TokenProvider
import costanza.george.reflect.operations.Deserializer
import costanza.george.reflect.operations.Serializer
import costanza.george.reflect.undoredo.Changer
import costanza.george.reflect.undoredo.Differ
import costanza.george.reflect.undoredo.GroupChange
import costanza.george.reflect.undoredo.IdAssigner
import costanza.george.ui.commands.ITool
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinext.js.js
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.html.unsafe
import org.w3c.dom.HTMLDivElement
import org.w3c.fetch.RequestInit
import react.*
import react.dom.div
import react.dom.jsStyle
import kotlin.js.Json


external interface DiagramProps : RProps {
    var tool: ITool
}

class DiagramState(
    var svg: String,
    var svg2: String,
    var svg3: String,
    var time: Int = 0,
    var x: Double = 0.0,
    var y: Double = 0.0,
    var cursor: String = "crosshair") : RState

@JsExport
class DiagramView(props: DiagramProps) : RComponent<DiagramProps, DiagramState>(props) {
    val together = Together()
    var diagram: Diagram
    val diagram2: Diagram
    val diagram3: Diagram
    val registry = ObjectTypeRegistry()
    val ids = IdAssigner()
    val calc = ClientTextCalculator()
    var marginX = 0.0
    var marginY = 0.0
    var startX = 0.0
    var startY = 0.0
    var x = 0.0
    var y = 0.0
    val mainScope = MainScope()

    init {

        mainScope.launch {
            val serial = fetchDiagram()
            diagram = makeDiagram(serial)
            setState { svg = together.makeSVG(diagram) }
        }

        registry.addAll(drawingEntityTypes)
        diagram = makeDiagram()
        diagram2 = together.makeDiagram2(calc)
        diagram3 = together.makeDiagram3(calc)
        state = DiagramState(together.makeSVG(diagram), together.makeSVG(diagram2), together.makeSVG(diagram3))

        val client = HttpClient {
            install(WebSockets)
        }
        mainScope.launch {
            val wsHost = window.origin.replace("http://", "").replace("https://", "")
            client.ws(
                method = HttpMethod.Get,
                host = "localhost:8080",
                path = "/diagram-changes"
            ) { // this: DefaultClientWebSocketSession
                send(ids.clientSession)
                while (true) {
                    when (val frame = incoming.receive()) {
                        is Frame.Text -> {
                            val json = JSON.parse<Json>(frame.readText())
                            val serial = json["payload"] as String
                            val forward = json["forward"] as Boolean
                            val changes: GroupChange = Deserializer(registry).deserialize(TokenProvider((serial)))
                            diagram.applyCollaborativeChanges(changes, forward)
                            setState { svg = together.makeSVG(diagram) }
                        }
                    }
                }
            }
        }
    }

    fun makeDiagram(serial: String? = null): Diagram {
        if (serial != null) {
            diagram = Deserializer(registry).deserialize(TokenProvider((serial)))
        } else {
            diagram = Diagram()
        }
        diagram.changer = Changer(ids.clientSession, registry, diagram)
        diagram.calc = calc
        diagram.differ = Differ(ids, registry, diagram)
        return diagram
    }

    fun <T> markMouse(e: antd.MouseEvent<T>) {
        val n = e.nativeEvent
        x = n.offsetX
        y = n.offsetY
    }

    private var ignore = false
    override fun RBuilder.render() {
        content {
            attrs.style = js { cursor = "${state.cursor}" }
            attrs {
                onMouseDown = { e ->
                    if (ignore) {
                        ignore = false
                    } else {
                        markMouse(e)
                        if (e.buttons == 2) {
                            ignore = true
                        }
                        if (e.buttons == 4) {
                            marginX = state.x
                            marginY = state.y
                            startX = e.clientX.toDouble()
                            startY = e.clientY.toDouble()
                            setState {
                                x = e.clientX.toDouble() - startX + marginX
                                y = e.clientY.toDouble() - startY + marginY
                                cursor = "grab"
                            }
                        } else if (e.buttons == 1) {
                            props.tool.diagram = diagram
                            props.tool.click(Coord(x, y))
                            val changes = diagram.recordChanges()
                            setState { svg = together.makeSVG(diagram) }
                            mainScope.launch {
                                sendChanges(changes, true)
                            }
                        }
                    }
                }
                onMouseUp = {
                    setState {  cursor = "crosshair" }
                }
                onMouseMove = { e ->
                    markMouse(e)
                    if (e.buttons == 4) {
                        setState {
                            x = e.clientX.toDouble() - startX + marginX
                            y = e.clientY.toDouble() - startY + marginY
                            cursor = "grabbing"
                        }
                    }
                }
                onContextMenu = { e: antd.MouseEvent<HTMLDivElement> ->
                    markMouse(e)
                    setState { time++ }
                }
            }

            div {
                dropdown {
                    attrs {
                        overlay = buildElement {
                            menu {
                                attrs.onClick = { inf: MenuInfo ->
                                    if (inf.key == "undo") {
                                        val changes = diagram.undo()
                                        setState { svg = together.makeSVG(diagram) }
                                        mainScope.launch {
                                            if (changes != null) {
                                                sendChanges(changes, false)
                                            }
                                        }
                                    }
                                    if (inf.key == "redo") {
                                        val changes = diagram.redo()
                                        setState { svg = together.makeSVG(diagram) }
                                        mainScope.launch {
                                            if (changes != null) {
                                                sendChanges(changes, true)
                                            }
                                        }
                                    }
                                }
                                menuItem {
                                    attrs {
                                        key = "undo"
                                        disabled = !diagram.canUndo()
                                    }
                                    +"Undo"
                                }
                                menuItem {
                                    attrs {
                                        key = "redo"
                                        disabled = !diagram.canRedo()
                                    }
                                    +"Redo"
                                }
                            }
                        }
                        trigger = arrayOf("contextMenu")

                        div {
                            div("overlay") {
                                attrs {
                                    jsStyle { top = state.y; left = state.x }
                                    unsafe { +state.svg }
                                }
                            }
                            div("overlay") {
                                attrs {
                                    jsStyle { top = state.y; left = state.x }
                                    unsafe { +state.svg2 }
                                }
                            }
                            div("overlay") {
                                attrs {
                                    unsafe { +state.svg3 }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.diagramview(handler: DiagramProps.() -> Unit): ReactElement {
    return child(DiagramView::class) {
        this.attrs(handler)
    }
}

suspend fun fetchDiagram(): String {
    val response = window
        .fetch("http://localhost:8080/diagram")
        .await()
        .json()
        .await()
    return (response as Json)["payload"] as String
}

suspend fun sendChanges(change: GroupChange, redo: Boolean) {
    window.fetch("/changes", object: RequestInit {
        override var method: String? = "PUT"
        override var body = JSON.stringify(
            js {payload = Serializer().serialize(change); forward = redo})
        override var headers = js {Accept = "application/json" }
    }).await()
}




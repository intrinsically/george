import antd.button.button
import antd.dropdown.dropdown
import antd.icon.highlightOutlined
import antd.layout.content
import antd.menu.*
import antd.tooltip.*
import costanza.george.diagrams.Together
import costanza.george.diagrams.base.Diagram
import costanza.george.diagrams.drawingEntityTypes
import costanza.george.geometry.Coord
import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.reflect.undoredo.Changer
import costanza.george.ui.commands.ITool
import costanza.george.utility.iloop
import kotlinx.html.unsafe
import org.w3c.dom.HTMLDivElement
import react.*
import react.dom.div
import react.dom.jsStyle
import kotlin.math.cos
import kotlin.math.sin


external interface DiagramProps : RProps {
    var tool: ITool
}

class DiagramState(
    var svg: String,
    var svg2: String,
    var svg3: String,
    var time: Int = 0,
    var visible: Boolean = false,
    var x: Double = 0.0,
    var y: Double = 0.0,
    var cursor: String = "crosshair") : RState

@JsExport
class DiagramView(props: DiagramProps) : RComponent<DiagramProps, DiagramState>(props) {
    val together = Together()
    val diagram: Diagram
    val diagram2: Diagram
    val diagram3: Diagram
    var marginX = 0.0
    var marginY = 0.0
    var startX = 0.0
    var startY = 0.0
    var x = 0.0
    var y = 0.0

    init {
        val calc = ClientTextCalculator()
        diagram = together.makeDiagram(calc)
        val reg = ObjectTypeRegistry()
        reg.addAll(drawingEntityTypes)
        diagram.changer = Changer(diagram, reg)
        diagram2 = together.makeDiagram2(calc)
        diagram3 = together.makeDiagram3(calc)
        state = DiagramState(together.makeSVG(diagram), together.makeSVG(diagram2), together.makeSVG(diagram3))
    }

    fun <T> markMouse(e: antd.MouseEvent<T>) {
        val n = e.nativeEvent
        x = n.offsetX
        y = n.offsetY
    }

    private var ignore = false
    override fun RBuilder.render() {
        content {
//            attrs.style = js { cursor = "${state.cursor}" }
            attrs {
                onMouseDown = { e ->
                    if (ignore) {
                        ignore = false
                    } else {
                        markMouse(e)
                        println("buttons = ${e.buttons}")
                        if (e.buttons == 2) {
                            ignore = true
                        }
                        if (e.buttons == 4) {
                            if (e.shiftKey) {
                                marginX = state.x
                                marginY = state.y
                                startX = e.clientX.toDouble()
                                startY = e.clientY.toDouble()
                                setState {
                                    x = e.clientX.toDouble() - startX + marginX
                                    y = e.clientY.toDouble() - startY + marginY
                                    cursor = "grab"
                                }
                            } else {
                                setState { visible = !visible }
                            }
                        } else if (e.buttons == 1) {
                            println("Creating")
                            props.tool.diagram = diagram
                            props.tool.click(Coord(x, y))
                            setState { svg = together.makeSVG(diagram) }
                        }
                    }
                }
                onMouseUp = {
                    setState {  cursor = "crosshair" }
                }
                onMouseMove = { e ->
                    markMouse(e)
                    if (e.buttons == 4 && e.shiftKey) {
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
                                        diagram.changer?.undo()
                                        setState { svg = together.makeSVG(diagram) }
                                    }
                                    if (inf.key == "redo") {
                                        diagram.changer?.redo()
                                        setState { svg = together.makeSVG(diagram) }
                                    }
                                }
                                menuItem {
                                    attrs {
                                        key = "1"
                                    }
                                    // work out the shape under the mouse
                                    val shape = diagram.locate(Coord(x, y))
                                    val title = if (shape == null) {
                                        "??"
                                    } else {
                                        shape.type() + " / " + (shape.name ?: "??")
                                    }
                                    +("Hello $x, $y -- $title")
                                }
                                menuItem {
                                    attrs {
                                        key = "undo"
                                    }
                                    +"Undo"
                                }
                                menuItem {
                                    attrs {
                                        key = "redo"
                                    }
                                    +"Redo"
                                }
                                subMenu {
                                    attrs {
                                        key = "4"
                                        title = "Submenu"
                                    }
                                    menuItem {
                                        attrs {
                                            key = "5"
                                        }
                                        +"Hello"
                                    }
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
                            if (state.visible) {
                                10.iloop { index ->
                                    div("overlay") {
                                        val theta = kotlin.math.PI * 2 / 10 * index
                                        val ty = y + state.y + sin(theta) * 60
                                        val tx = x + state.x + cos(theta) * 60
                                        attrs.jsStyle {
                                            top = ty - 10; left = tx - 20
                                        }

                                        tooltip {
                                            attrs.title = "Class $index"
                                            button {
                                                attrs {
                                                    shape = "circle"
                                                    icon = buildElement {
                                                        highlightOutlined {}
                                                    }
                                                }
                                            }
                                        }
                                    }
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


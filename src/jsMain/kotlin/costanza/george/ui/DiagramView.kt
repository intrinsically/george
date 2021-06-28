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
import costanza.george.reflect.undoredo.Differ
import costanza.george.reflect.undoredo.IdAssigner
import costanza.george.ui.commands.ITool
import costanza.george.utility.iloop
import kotlinext.js.js
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
        diagram.differ = Differ(IdAssigner(), reg, diagram)
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
                            diagram.recordChanges()
                            setState { svg = together.makeSVG(diagram) }
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
                                        diagram.undo()
                                        setState { svg = together.makeSVG(diagram) }
                                    }
                                    if (inf.key == "redo") {
                                        diagram.redo()
                                        setState { svg = together.makeSVG(diagram) }
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


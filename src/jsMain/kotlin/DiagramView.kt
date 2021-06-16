import antd.button.button
import antd.dropdown.dropdown
import antd.icon.highlightOutlined
import antd.layout.content
import antd.menu.menu
import antd.menu.menuItem
import antd.menu.subMenu
import costanza.Together
import costanza.geometry.Coord
import costanza.utility.iloop
import costanza.diagrams.base.Diagram
import kotlinext.js.js
import kotlinx.html.unsafe
import org.w3c.dom.HTMLDivElement
import react.*
import react.dom.div
import react.dom.jsStyle
import kotlin.math.cos
import kotlin.math.sin
import antd.tooltip.*



external interface DiagramProps : RProps {
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
        diagram2 = together.makeDiagram2(calc)
        diagram3 = together.makeDiagram3(calc)
        state = DiagramState(together.makeSVG(diagram), together.makeSVG(diagram2), together.makeSVG(diagram3))
    }

    fun <T> markMouse(e: antd.MouseEvent<T>) {
        val n = e.nativeEvent
        x = n.offsetX
        y = n.offsetY
    }

    override fun RBuilder.render() {
        content {
            attrs.style = js { cursor = "${state.cursor}" }
            attrs {
                onMouseDown = { e: antd.MouseEvent<HTMLDivElement> ->
                    markMouse(e)
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
                    }
                }
                onMouseUp = {
                    setState {  cursor = "crosshair" }
                }
                onMouseMove = { e: antd.MouseEvent<HTMLDivElement> ->
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
                                subMenu {
                                    attrs {
                                        key = "4"
                                        title = "Submenu"
                                    }
                                    menuItem {
                                        attrs {
                                            key = "2"
                                        }
                                        +"One"
                                    }
                                    if (state.time % 2 == 0) {
                                        menuItem {
                                            attrs {
                                                key = "3"
                                            }
                                            +"Two"
                                        }
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


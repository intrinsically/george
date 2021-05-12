import antd.dropdown.dropdown
import antd.layout.content
import antd.menu.menu
import antd.menu.menuItem
import antd.menu.subMenu
import costanza.Together
import costanza.geometry.Coord
import diagrams.base.Diagram
import kotlinext.js.js
import kotlinx.css.margin
import kotlinx.css.style
import kotlinx.html.unsafe
import org.w3c.dom.HTMLDivElement
import react.*
import react.dom.div
import react.dom.jsStyle
import react.dom.style
import styled.css
import styled.styledDiv


external interface DiagramProps : RProps {
}

class DiagramState(val svg: String, val svg2: String, val svg3: String, val time: Int, val x: Double = 0.0, val y: Double = 0.0, val cursor: String = "crosshair") : RState

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
        state = DiagramState(together.makeSVG(diagram), together.makeSVG(diagram2), together.makeSVG(diagram3), 0)
    }

    override fun RBuilder.render() {
        content {
            attrs.style = js { cursor = "${state.cursor}" }
            attrs {
                onMouseDown = { e: antd.MouseEvent<HTMLDivElement> ->
                    if (e.buttons == 4) {
                        marginX = state.x
                        marginY = state.y
                        startX = e.clientX.toDouble()
                        startY = e.clientY.toDouble()
                        setState(
                            DiagramState(
                                state.svg,
                                state.svg2,
                                state.svg3,
                                state.time,
                                e.clientX.toDouble() - startX + marginX,
                                e.clientY.toDouble() - startY + marginY,
                                "grab"
                            )
                        )

                    }
                }
                onMouseUp = { e: antd.MouseEvent<HTMLDivElement> ->
                    setState(
                        DiagramState(
                            state.svg,
                            state.svg2,
                            state.svg3,
                            state.time,
                            state.x,
                            state.y,
                            "crosshair"
                        )
                    )
                }
                onMouseMove = { e: antd.MouseEvent<HTMLDivElement> ->
                    if (e.buttons == 4) {
                        setState(
                            DiagramState(
                                state.svg,
                                state.svg2,
                                state.svg3,
                                state.time,
                                e.clientX.toDouble() - startX + marginX,
                                e.clientY.toDouble() - startY + marginY,
                                "grabbing"
                            )
                        )
                    }
                }
                onContextMenu = { e: antd.MouseEvent<HTMLDivElement> ->
                    val n = e.nativeEvent
                    x = n.offsetX
                    y = n.offsetY
                    // make the menu rebuild
                    setState(DiagramState(state.svg, state.svg2, state.svg3, state.time + 1, state.x, state.y))
                }
            }

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
                                jsStyle { margin = "${state.y}px 0 0 ${state.x}px" }
                                unsafe { +state.svg }
                            }
                        }
                        div("overlay") {
                            attrs {
                                jsStyle { margin = "${state.y}px 0 0 ${state.x}px" }
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

fun RBuilder.diagramview(handler: DiagramProps.() -> Unit): ReactElement {
    return child(DiagramView::class) {
        this.attrs(handler)
    }
}


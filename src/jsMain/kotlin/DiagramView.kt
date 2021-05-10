import antd.dropdown.dropdown
import antd.layout.content
import antd.menu.menu
import antd.menu.menuItem
import antd.menu.subMenu
import costanza.Together
import costanza.geometry.Coord
import diagrams.base.Diagram
import kotlinx.css.margin
import kotlinx.html.unsafe
import org.w3c.dom.HTMLDivElement
import react.*
import react.dom.div
import styled.css
import styled.styledDiv


external interface DiagramProps : RProps {
}

class DiagramState(val svg: String, val time: Int, val x: Double = 0.0, val y: Double = 0.0) : RState

@JsExport
class DiagramView(props: DiagramProps) : RComponent<DiagramProps, DiagramState>(props) {
    val together = Together()
    val diagram: Diagram
    var marginX = 0.0
    var marginY = 0.0
    var startX = 0.0
    var startY = 0.0
    var x = 0.0
    var y = 0.0
    var dragX = 0
    var dragY = 0
    var n = 0

    init {
        val calc = ClientTextCalculator()
        diagram = together.makeDiagram(calc)
        state = DiagramState(together.makeSVG(diagram), 0)
        val json = together.serialize(diagram)
    }

    override fun RBuilder.render() {
        content {
            attrs {
                onMouseDown = { e: antd.MouseEvent<HTMLDivElement> ->
                    if (e.buttons == 1) {
                        marginX = state.x
                        marginY = state.y
                        startX = e.clientX.toDouble()
                        startY = e.clientY.toDouble()
                    }
                }
                onMouseMove = { e: antd.MouseEvent<HTMLDivElement> ->
                    if (e.buttons == 1) {
                        setState(
                            DiagramState(
                                state.svg,
                                state.time,
                                e.clientX.toDouble() - startX + marginX,
                                e.clientY.toDouble() - startY + marginY
                            )
                        )
                    }
                }
                onContextMenu = { e: antd.MouseEvent<HTMLDivElement> ->
                    val n = e.nativeEvent
                    x = n.offsetX
                    y = n.offsetY
                    console.log(e)
                    // make the menu rebuild
                    setState(DiagramState(state.svg, state.time + 1, state.x, state.y))
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
                            styledDiv {
                                this
                                css { +ScreenLayoutStyles.overlay }
                            }
                        }
                        styledDiv {
                            css {
                                val ix = state.x
                                val iy = state.y
                                margin = "${iy}px 0 0 ${ix}px"
                            }
                            div(classes = "background") {
                                attrs.unsafe {
                                    +state.svg
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


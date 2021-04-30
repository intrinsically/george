import antd.dropdown.dropdown
import antd.layout.content
import antd.menu.menu
import antd.menu.menuItem
import antd.menu.subMenu
import costanza.Together
import costanza.geometry.Coord
import diagrams.base.Diagram
import kotlinx.browser.document
import kotlinx.css.margin
import kotlinx.html.unsafe
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
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

    init {
        val calc = ClientTextCalculator()
        diagram = together.makeDiagram(calc)
        state = DiagramState(together.makeSVG(diagram), 0)
        val json = together.serialize(diagram)
    }

    private fun click(e: Event) {
//        e.preventDefault()
//        if (e is MouseEvent && e.button == 0.toShort()) {
//        }
    }

    var dragX = 0
    var dragY = 0
    private fun mouseDown(e: Event) {
        console.log(e)
        if (e is MouseEvent && e.button == 1.toShort()) {
            marginX = state.x
            marginY = state.y
            startX = e.clientX.toDouble()
            startY = e.clientY.toDouble()
        }
    }

    var n = 0
    private fun mouseMove(e: Event) {
        if (e is MouseEvent && e.button == 1.toShort()) {
            setState(DiagramState(state.svg, state.time, e.clientX - startX + marginX, e.clientY - startY + marginY))
        }
    }

    private fun mouseUp(e: Event) {
        if (e is MouseEvent && e.button == 1.toShort()) {
        }
    }

    private fun contextMenu(e: Event) {
        if (e is MouseEvent) {
            x = e.offsetX
            y = e.offsetY
            // make the menu rebuild
            setState(DiagramState(state.svg, state.time + 1, state.x, state.y))
        }
    }

    override fun componentDidMount() {
        document.addEventListener("click", ::click)
        document.addEventListener("contextmenu", ::contextMenu)
        document.addEventListener("mousedown", ::mouseDown)
        document.addEventListener("mousemove", ::mouseMove)
        document.addEventListener("mouseup", ::mouseUp)
    }

    override fun componentWillUnmount() {
        document.removeEventListener("click", ::click)
        document.removeEventListener("contextMenu", ::contextMenu)
        document.removeEventListener("mousedown", ::mouseDown)
        document.removeEventListener("mousemove", ::mouseMove)
        document.removeEventListener("mouseup", ::mouseUp)
    }

    override fun RBuilder.render() {
        content {
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


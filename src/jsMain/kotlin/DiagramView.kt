import antd.dropdown.dropdown
import antd.layout.content
import antd.menu.menu
import antd.menu.menuItem
import antd.menu.subMenu
import costanza.Together
import costanza.geometry.Coord
import diagrams.base.Diagram
import kotlinx.browser.window
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onContextMenuFunction
import kotlinx.html.onClick
import kotlinx.html.unsafe
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import react.*
import react.dom.div
import styled.css
import styled.styledDiv


external interface DiagramProps : RProps {
}

class DiagramState(val svg: String, val time: Int, val scrollLeft: Double, val scrollRight: Double) : RState

@JsExport
class DiagramView(props: DiagramProps) : RComponent<DiagramProps, DiagramState>(props) {
    val together = Together()
    val diagram: Diagram
    var x = 0.0
    var y = 0.0

    init {
        val calc = ClientTextCalculator()
        diagram = together.makeDiagram(calc)
        state = DiagramState(together.makeSVG(diagram), 0, 100.0, 100.0)
        val json = together.serialize(diagram)
    }

    private fun clickHandler(e: Event) {
        if (e is MouseEvent && e.button == 0.toShort()) {
            println("Clicked")
        }
    }

    private fun contextMenuHandler(e: Event) {
        if (e is MouseEvent) {
            x = e.offsetX
            y = e.offsetY
            // make the menu rebuild
            setState(DiagramState(state.svg, state.time + 1, x, y))
            println("Scroll modified")
        }
    }

    override fun RBuilder.render() {
        content {
            val a: dynamic = this
            a.scrollLeft = state.scrollLeft
            a.scrollRight = state.scrollRight
            +a.scrollLeft
            println("Inside render")

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
                        attrs.onClick = "click"
                        attrs.onClickFunction = ::clickHandler
                        attrs.onContextMenuFunction = ::contextMenuHandler
                        div("overlay") {
                            styledDiv {
                                css { +ScreenLayoutStyles.overlay }
                            }
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

fun RBuilder.diagramview(handler: DiagramProps.() -> Unit): ReactElement {
    return child(DiagramView::class) {
        this.attrs(handler)
    }
}


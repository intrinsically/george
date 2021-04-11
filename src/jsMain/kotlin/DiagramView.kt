import antd.dropdown.dropdown
import costanza.app.Together
import diagrams.base.Diagram
import kotlinx.html.unsafe
import react.*
import react.dom.div
import antd.menu.*
import costanza.geometry.Coord
import kotlinx.browser.document
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent

external interface DiagramProps : RProps {
}

class DiagramState(val svg: String, val time: Int) : RState

@JsExport
class DiagramView(props: DiagramProps) : RComponent<DiagramProps, DiagramState>(props) {
    val together = Together()
    val diagram: Diagram
    var x = 0.0
    var y = 0.0

    init {
        val calc = ClientTextCalculator()
        diagram = together.makeDiagram(calc)
        state = DiagramState(together.makeSVG(diagram), 0)
        val json = together.makeJSON(diagram)
        println(json)
        console.log(diagram)
    }

    private fun click(e: Event) {
        e.preventDefault()
        if (e is MouseEvent) {
            val a: dynamic = e
            x = a.layerX
            y = a.layerY
        }
    }

    private fun contextMenu(e: Event) {
        if (e is MouseEvent) {
            val a: dynamic = e
            x = a.layerX
            y = a.layerY
            // make the menu rebuild
            setState(DiagramState(state.svg, state.time + 1))
        }
    }

    override fun componentDidMount() {
        document.addEventListener("click", ::click)
        document.addEventListener("contextmenu", ::contextMenu)
    }

    override fun componentWillUnmount() {
        document.removeEventListener("click", ::click)
        document.removeEventListener("contextMenu", ::contextMenu)
    }

    override fun RBuilder.render() {
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
                            val title = if (shape == null) { "??" } else { shape.type() + " / " + (shape.name ?: "??") }
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
            }
            div(classes = "background") {
                attrs {
                    unsafe {
                        +state.svg
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


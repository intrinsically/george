import antd.dropdown.dropdown
import antd.form.FormInstance
import antd.layout.content
import costanza.Together
import diagrams.base.Diagram
import kotlinx.html.unsafe
import react.*
import react.dom.div
import antd.menu.*
import costanza.geometry.Coord
import kotlinext.js.js
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.css.*
import kotlinx.css.properties.LineHeight
import kotlinx.html.DIV
import kotlinx.html.classes
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import react.dom.findDOMNode
import styled.StyleSheet
import styled.css
import styled.styledDiv
import styled.styledSpan


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
        val json = together.serialize(diagram)
    }

    private fun click(e: Event) {
        e.preventDefault()
        if (e is MouseEvent && e.button == 0.toShort()) {
            x = e.offsetX
            y = e.offsetY
        }
    }

    var dragX = 0
    var dragY = 0
    private fun mouseDown(e: Event) {
        if (e is MouseEvent && e.button == 1.toShort()) {
            x = e.offsetX
            y = e.offsetY
        }
    }

    private fun mouseMove(e: Event) {
        if (e is MouseEvent && e.button == 1.toShort()) {
            x = e.offsetX
            y = e.offsetY

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
            setState(DiagramState(state.svg, state.time + 1))
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


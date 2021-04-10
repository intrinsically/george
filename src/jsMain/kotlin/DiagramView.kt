import costanza.app.Together
import diagrams.base.Diagram
import kotlinx.html.unsafe
import org.w3c.dom.HTMLDivElement
import react.*
import react.dom.div

external interface DiagramProps : RProps {
}

class DiagramState(val diagram: Diagram) : RState

@JsExport
class DiagramView(props: DiagramProps) : RComponent<DiagramProps, DiagramState>(props) {
    val together = Together()

    init {
        val calc = ClientTextCalculator()
        state = DiagramState(together.makeDiagram(calc))
    }

    override fun RBuilder.render() {
        div(classes="background") {
            attrs {
                unsafe {
                    +together.makeSVG(state.diagram)}
            }
        }
    }
}

fun RBuilder.diagramview(handler: DiagramProps.() -> Unit): ReactElement {
    return child(DiagramView::class) {
        this.attrs(handler)
    }
}


import costanza.app.Together
import kotlinx.html.unsafe
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div
import antd.input.*
import styled.styledDiv

external interface WelcomeProps : RProps {
    var svg: String
}

data class WelcomeState(val json: String) : RState

@JsExport
class Welcome(props: WelcomeProps) : RComponent<WelcomeProps, WelcomeState>(props) {
    var json: String = ""
    val together = Together()
    val calc = ClientTextCalculator()

    init {
        // make the svg and json
        val diag = together.makeDiagram(calc)
        json = together.makeJSON(diag)
        state = WelcomeState(json)
    }

    override fun RBuilder.render() {
        styledDiv {
            textArea {
                attrs {
                    rows = 5
                    defaultValue = json
                    onChange = { e: dynamic ->
                        console.log("Value is")
                        console.log(e.target.textContent)
                        setState(WelcomeState(e.target.textContent))
                    }
                }

            }
        }
        div {
            attrs {
                unsafe {
                    val diag = together.makeDiag(calc, state.json)
                    val svg = together.makeSVG(diag)

                    +svg }
            }
        }
    }
}

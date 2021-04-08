import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import antd.button.button
import antd.calendar.calendar
import antd.card.card
import costanza.app.Together
import costanza.diagrams.base.FontDetails
import costanza.diagrams.base.ITextCalculator
import kotlinx.css.*
import kotlinx.css.properties.LineHeight
import kotlinx.html.unsafe
import react.dom.*
import styled.StyleSheet
import styled.css
import styled.styledDiv

external interface WelcomeProps : RProps {
    var name: String
}

data class WelcomeState(val name: String) : RState

class SimpleTextCalculator: ITextCalculator {
    override fun calcHeight(details: FontDetails): Double {
        return 16.0
    }

    override fun calcWidth(details: FontDetails, minWidth: Double, text: String): Double {
        return minWidth + 5.0
    }

}

@JsExport
class Welcome(props: WelcomeProps) : RComponent<WelcomeProps, WelcomeState>(props) {
    var json: String = ""
    var svg: String = ""

    init {
        state = WelcomeState(props.name)

        // make the svg and json
        val together = Together()
        val calc = SimpleTextCalculator()
        val diag = together.makeDiagram(calc)
        json = together.makeJSON(diag)
        svg = together.makeSVG(diag)
    }

    override fun RBuilder.render() {
        div {
            +"Hello Andrew, ${state.name}"
        }
        styledDiv {
            button {
                attrs.type = "primary"
                +"Primary"
            }
            button { +"Default" }
            button {
                attrs.type = "dashed"
                +"Dashed"
            }
            button {
                attrs.type = "danger"
                +"Danger Will Robinson"
            }
            button {
                attrs.type = "link"
                +"Link"
            }
        }
        div {
            attrs {
                unsafe { +svg }
            }
        }
    }
}

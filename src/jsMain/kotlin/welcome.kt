import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div
import react.dom.input
import antd.button.button
import antd.calendar.calendar
import kotlinx.css.*
import kotlinx.css.properties.LineHeight
import styled.StyleSheet
import styled.css
import styled.styledDiv

external interface WelcomeProps : RProps {
    var name: String
}

data class WelcomeState(val name: String) : RState

@JsExport
class Welcome(props: WelcomeProps) : RComponent<WelcomeProps, WelcomeState>(props) {

    init {
        state = WelcomeState(props.name)
    }

    override fun RBuilder.render() {
        div {
            +"Hello, ${state.name}"
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
                +"Danger"
            }
            button {
                attrs.type = "link"
                +"Link"
            }
        }
        styledDiv {
            calendar {
            }
        }
        input {
            attrs {
                type = InputType.text
                value = state.name
                onChangeFunction = { event ->
                    setState(
                        WelcomeState(name = (event.target as HTMLInputElement).value)
                    )
                }
            }
        }
    }
}

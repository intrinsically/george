import react.dom.render
import kotlinx.browser.document
import kotlinx.browser.window

fun main() {
    kotlinext.js.require("antd/dist/antd.css")

    window.onload = {
        render(document.getElementById("root")) {
            child(Welcome::class) {
                attrs {
                    svg = "Kotlin/JS"
                }
            }
        }
    }
}

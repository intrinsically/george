import costanza.george.diagrams.Together
import costanza.george.app.G2DTextCalculator
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.http.content.resources
import io.ktor.http.content.static
import kotlinx.html.*

fun HTML.index() {
    head {
        title("Costanza")
    }
    body {
        div {
            id = "root"
        }
        script(src = "/static/js.js") {}
    }
}

fun main() {
    val calc = G2DTextCalculator()
    val together = Together()
    val diagram = together.makeDiagram(calc)
    println(together.serialize(diagram))

    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        routing {
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }
            static("/static") {
                resources()
            }
        }
    }.start(wait = true)
}

fun server.module() {
    install(WebSockets)
    routing {
        webSocket("/") {
            send("You are connected!")
            for(frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                send("You said: $receivedText")
            }
        }
    }
}

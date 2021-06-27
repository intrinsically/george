import costanza.george.diagrams.Together
import costanza.george.app.G2DTextCalculator
import costanza.george.server.Connection
import io.ktor.application.call
import io.ktor.application.*
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.http.content.resources
import io.ktor.http.content.static
import kotlinx.html.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import java.time.Duration
import java.util.*

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

@ExperimentalWebSocketExtensionApi
fun Application.module() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(60) // Disabled (null) by default
        timeout = Duration.ofSeconds(15)
        maxFrameSize = kotlin.Long.MAX_VALUE // Disabled (max value). The connection will be closed if surpassed this length.
        masking = false

        extensions {
            // install(...)
        }
    }

    routing {
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
        webSocket("/") {
            println("Adding user!")
            val thisConnection = Connection(this)
            connections += thisConnection
            try {
                send("You are connected! There are ${connections.count()} users here.")
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    val textWithUsername = "[${thisConnection.name}]: $receivedText"
                    connections.forEach {
                        it.session.send(textWithUsername)
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing $thisConnection!")
                connections -= thisConnection
            }
        }
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
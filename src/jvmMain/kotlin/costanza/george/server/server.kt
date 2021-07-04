package costanza.george.server

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import costanza.george.diagrams.Together
import costanza.george.diagrams.drawingEntityTypes
import costanza.george.reflect.ObjectTypeRegistry
import costanza.george.reflect.TokenProvider
import costanza.george.reflect.operations.Deserializer
import costanza.george.reflect.operations.Serializer
import costanza.george.reflect.undoredo.Changer
import costanza.george.reflect.undoredo.GroupChange
import costanza.george.utility._list
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
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

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    val calc = G2DTextCalculator()
    val together = Together()
    val diagram = together.makeDiagram(calc)
    val registry = ObjectTypeRegistry()
    val deserializer = Deserializer(registry)

    registry.addAll(drawingEntityTypes)
    diagram.changer = Changer("", registry, diagram)

    // note: installing CORS breaks it on gitpod, need to investigate
    install(WebSockets)
    install(Compression)

    val connections = _list<WebSocketServerSession>()
    routing {
        get("/") {
            call.respondHtml(HttpStatusCode.OK, HTML::index)
        }
        get("/diagram") {
            val str = Serializer().serialize(diagram)
            val json = Gson().toJson(JsonPayload(str, true))
            call.respond(json)
        }
        put("/changes") {
            val str = call.receive<String>()
            val details = Gson().fromJson(str, JsonPayload::class.javaObjectType)
            val changes: GroupChange = deserializer.deserialize(TokenProvider(details.payload))
            diagram.applyCollaborativeChanges(changes, details.forward)
            call.respond("ok")
            val remove = _list<WebSocketServerSession>()
            connections.forEach {
                try {
                    it.send(Frame.Text(str))
                } catch (ex: Exception) {
                    // remove from the list
                    remove.add(it)
                }
            }
            connections -= remove
        }

        webSocket("/diagram-changes") {
            connections += this
            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        val clientSession = frame.readText()
                        println("Client $clientSession registered")
                    }
                }
            }
        }

        static("/static") {
            resources()
        }
    }
}

data class JsonPayload(
    @SerializedName("payload") val payload: String,
    @SerializedName("forward") val forward: Boolean
)
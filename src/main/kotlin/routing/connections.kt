package routing

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import java.util.concurrent.ConcurrentHashMap

val connections = ConcurrentHashMap<String, WebSocketSession>()

fun Application.configureRouting() {
    routing {
        webSocket("/chat") {  // Connect to WebSocket
            val sessionId = this.hashCode().toString()
            connections[sessionId] = this
            try {
                incoming.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        val message = frame.readText()
                        connections.values.forEach { session ->
                            session.send("User[$sessionId]: $message")
                        }
                    }
                }
            } finally {
                connections.remove(sessionId)
            }
        }
    }
}

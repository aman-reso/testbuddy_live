package livetest

import authorization.JwtUtils.getUserId
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Application.configureWebSockets() {
    routing {
        webSocket("/live-participants/{testId}/{name}") {
            val sessionId = this.hashCode().toString()
            val testId = call.parameters["testId"]?.toIntOrNull() ?: return@webSocket close(
                CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Invalid testId")
            )
            val name = call.parameters["name"] ?: return@webSocket close(
                CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Name not provided")
            )

            WebSocketManager.addParticipant(sessionId, this, testId, name)
            WebSocketManager.broadcastUserCount(testId)

            try {
                incoming.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        WebSocketManager.broadcastUserCount(testId) // Update count on each message if needed
                    }
                }
            } finally {
                WebSocketManager.removeParticipant(sessionId)
                WebSocketManager.broadcastUserCount(testId) // Send updated count after user leaves
            }
        }

    }
}

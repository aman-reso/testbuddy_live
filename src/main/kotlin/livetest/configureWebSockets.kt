package livetest

import authorization.JwtUtils.getUserId
import com.mysql.cj.log.Log
import dao.Subjects
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureWebSockets() {
    val x = Subjects.selectAll()
    println(x)
    routing {
        get("/"){
            transaction {
                val subjects = Subjects.select(where = Subjects.subjectName eq  "Chemistry").map { row ->
                    (row[Subjects.id] to row[Subjects.subjectName]) to row[Subjects.createdAt]
                }
                println(subjects)
            }
        }
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

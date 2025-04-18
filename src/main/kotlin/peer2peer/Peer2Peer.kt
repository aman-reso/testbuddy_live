package peer2peer

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.jetbrains.exposed.sql.*
import peer2peer.data.WaitingUser
import peer2peer.data.handleWebSocketMsg
import java.time.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap

//object QuizParticipants : IdTable<String>() {
//    val quizId = long("quiz_id").references(Quizzes.id)
//    val userId = varchar("user_id", 255)
//    val isBot = bool("is_bot").default(false)
//    val joinedAt = datetime("joined_at").clientDefault { java.time.LocalDateTime.now() }
//    override val id = TestParticipantsTesting.varchar("id", 36).clientDefault { UUID.randomUUID().toString() }.entityId()
//    override val primaryKey = PrimaryKey(id)
//}

suspend inline fun <reified T> DefaultWebSocketServerSession.sendSerialized(data: T) {
    val jsonString = Json.encodeToString(data)
    send(Frame.Text(jsonString))
}

val sessions = ConcurrentHashMap<String, DefaultWebSocketServerSession>()
val json = Json { ignoreUnknownKeys = true }

fun Application.peer2Peer() {
    routing {
        webSocket("/p2p") {
            val userId = call.parameters["user"] ?: return@webSocket
            sessions[userId] = this
            handleWebSocketMsg(userId, this)
        }
    }
}


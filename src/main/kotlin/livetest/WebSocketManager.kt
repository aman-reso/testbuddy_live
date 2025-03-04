package livetest

import dao.addTestParticipant
import io.ktor.websocket.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

object WebSocketManager {
    private val participants = mutableMapOf<Int, MutableMap<String, WebSocketSession>>()
    private val lock = Mutex()

    suspend fun addParticipant(sessionId: String, session: WebSocketSession, testId: Int, name: String) {
        lock.withLock {
            val testParticipants = participants.getOrPut(testId) { mutableMapOf() }
            testParticipants[sessionId] = session
        }
        addTestParticipant(UUID.randomUUID(), "any$sessionId")
    }

    suspend fun removeParticipant(sessionId: String) {
        lock.withLock {
            val iterator = participants.iterator()
            while (iterator.hasNext()) {
                val (testId, sessions) = iterator.next()
                if (sessions.remove(sessionId) != null && sessions.isEmpty()) {
                    iterator.remove()
                }
            }
        }
    }

    suspend fun broadcast(message: String, testId: Int) {
        lock.withLock {
            participants[testId]?.values?.forEach { session ->
                try {
                    session.send(Frame.Text(message))
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private suspend fun getParticipantCount(testId: Int): Int {
        return lock.withLock {
            participants[testId]?.size ?: 0
        }
    }

    suspend fun broadcastUserCount(testId: Int) {
        val count = getParticipantCount(testId)
        val message = Json.encodeToString(UserCountMessage(joinedUserCount = count))
        participants.values.forEach { session ->
            session.values.forEach {
                it.send(Frame.Text(message))
            }
        }
    }
}


@Serializable
data class UserCountMessage(val joinedUserCount: Int)
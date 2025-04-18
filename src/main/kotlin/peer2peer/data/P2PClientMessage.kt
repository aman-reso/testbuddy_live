package peer2peer.data

import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import peer2peer.json
import peer2peer.sessions

@Serializable
data class P2PClientMessage(val eventType: P2PEventType, val testId: String? = null, val subjectId: String? = null)

@Serializable
enum class P2PEventType {
    @SerialName("fetch_tests")
    FETCH_TESTS,

    @SerialName("test_list")
    SEND_TEST_LIST,

    @SerialName("find_opponent")
    FIND_OPPONENT
}

suspend fun handleWebSocketMsg(userId: String, session: DefaultWebSocketServerSession) {
    try {
        for (frame in session.incoming) {
            if (frame is Frame.Text) {
                val message = json.decodeFromString<P2PClientMessage>(frame.readText())
                when (message.eventType) {
                    P2PEventType.FETCH_TESTS -> {
                        handleFetchTests(session)
                    }

                    P2PEventType.FIND_OPPONENT -> {
                        message.testId?.let {
                            handleFindOpponent(WaitingUser(userId, session, it))
                        }
                    }

                    else -> {
                        handleFetchTests(session)
                    }
                }
            }
        }
    } catch (e: Exception) {
        println(e.message)
        e.printStackTrace()
    }
}
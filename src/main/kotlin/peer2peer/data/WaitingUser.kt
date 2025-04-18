package peer2peer.data

import io.ktor.server.websocket.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import peer2peer.json
import peer2peer.sendSerialized

data class WaitingUser(
    val userId: String,
    val session: DefaultWebSocketServerSession,
    val testId: String
)

enum class UserState {
    IDLE,
    WAITING,
    MATCHED
}

val userStates = mutableMapOf<String, UserState>()

val waitingUsersByTestId = mutableMapOf<String, MutableList<WaitingUser>>()

suspend fun handleFindOpponent(currentUser: WaitingUser) {
    val testId = currentUser.testId

    if (userStates[currentUser.userId] == UserState.WAITING) {
        currentUser.session.sendSerialized(
            LiveP2PGenericResponse(
                eventType = P2PEventType.FIND_OPPONENT,
                jsonObject = buildJsonObject {
                    put("message", "Already waiting for opponent.")
                    put("testId", testId)
                }
            )
        )
        return
    }

    if (userStates[currentUser.userId] == UserState.MATCHED) {
        currentUser.session.sendSerialized(
            LiveP2PGenericResponse(
                eventType = P2PEventType.FIND_OPPONENT,
                jsonObject = buildJsonObject {
                    put("message", "Already in a game.")
                    put("testId", testId)
                }
            )
        )
        return
    }
    userStates[currentUser.userId] = UserState.WAITING
    val waitingList = waitingUsersByTestId.getOrPut(testId) { mutableListOf() }

    val opponent = waitingList.firstOrNull { it.userId != currentUser.userId }

    if (opponent != null) {
        waitingList.remove(opponent)
        userStates[currentUser.userId] = UserState.MATCHED
        userStates[opponent.userId] = UserState.MATCHED
        sendMatchFound(currentUser, opponent, testId)
    } else {
        waitingList.add(currentUser)

        currentUser.session.sendSerialized(
            LiveP2PGenericResponse(
                eventType = P2PEventType.FIND_OPPONENT,
                jsonObject = buildJsonObject {
                    put("message", "Waiting for opponent...")
                    put("testId", testId)
                }
            )
        )

        println("${currentUser.userId} is waiting for opponent on test $testId")

        withTimeoutOrNull(60_000) {
            while (true) {
                delay(500)
                if (userStates[currentUser.userId] == UserState.MATCHED) {
                    return@withTimeoutOrNull
                }

                val matchedOpponent = waitingList.firstOrNull {
                    it.userId != currentUser.userId
                }

                if (matchedOpponent != null) {
                    waitingList.remove(matchedOpponent)
                    waitingList.remove(currentUser)
                    userStates[currentUser.userId] = UserState.MATCHED
                    userStates[matchedOpponent.userId] = UserState.MATCHED
                    sendMatchFound(currentUser, matchedOpponent, testId)
                    return@withTimeoutOrNull
                }
            }

        } ?: run {
            waitingList.remove(currentUser)
            userStates[currentUser.userId] = UserState.IDLE
            println("Timeout: ${currentUser.userId} could not find opponent for test $testId")

            currentUser.session.sendSerialized(
                LiveP2PGenericResponse(
                    eventType = P2PEventType.FIND_OPPONENT,
                    jsonObject = buildJsonObject {
                        put("message", "No opponent found within 60 seconds.")
                        put("testId", testId)
                    }
                )
            )
        }
    }
}


suspend fun sendMatchFound(user1: WaitingUser, user2: WaitingUser, testId: String) {
    val msg1 = LiveP2PGenericResponse(
        eventType = P2PEventType.FIND_OPPONENT,
        jsonObject = buildJsonObject {
            put("opponentId", user2.userId)
            put("testId", testId)
        }
    )

    val msg2 = LiveP2PGenericResponse(
        eventType = P2PEventType.FIND_OPPONENT,
        jsonObject = buildJsonObject {
            put("opponentId", user1.userId)
            put("testId", testId)
        }
    )

    user1.session.sendSerialized(msg1)
    user2.session.sendSerialized(msg2)

    println("Matched ${user1.userId} with ${user2.userId} for test $testId")
}



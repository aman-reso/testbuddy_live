package peer2peer.data

import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import peer2peer.json

@Serializable
data class LiveP2PQuizSubjectResponse(
    val id: String,
    val name: String,
    val subTitle: String,
    val subjectName: String,
    val subjectId: String,
    val bannerUrl: String?,
    val isActive: Boolean,
    val numberOfQuestion: Int
)


suspend fun handleFetchTests(session: DefaultWebSocketServerSession) {
    val mockTests = listOf(
        LiveP2PQuizSubjectResponse(
            id = "1",
            name = "Technology",
            subTitle = "From novice to tech master!",
            subjectName = "Tech",
            subjectId = "tech_001",
            bannerUrl = "https://example.com/tech_banner.png",
            isActive = true,
            numberOfQuestion = 10
        ),
        LiveP2PQuizSubjectResponse(
            id = "2",
            name = "Science",
            subTitle = "Discover scientific wonders",
            subjectName = "Science",
            subjectId = "science_001",
            bannerUrl = "https://example.com/science_banner.png",
            isActive = true,
            numberOfQuestion = 15
        )
    )

    val response = LiveP2PGenericResponse(P2PEventType.SEND_TEST_LIST, json.encodeToString(mockTests), status = "OK")
    session.send(Frame.Text(json.encodeToString(response)))
}

package peer2peer.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class LiveP2PGenericResponse(
    val eventType: P2PEventType,
    val data: String? = null,
    val status: String? = "OK",
    val jsonObject: JsonObject? = null
)
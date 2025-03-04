package livetest

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.concurrent.ConcurrentHashMap

@OptIn(DelicateCoroutinesApi::class)
object LiveParticipantManager {
    private val participantsCache = ConcurrentHashMap<Int, String>()
    private const val updateInterval = 5000L

    init {
        GlobalScope.launch {
            while (true) {
                delay(updateInterval)
                flushToDatabase()
            }
        }
    }

    fun addParticipant(testId: Int, name: String) {
        participantsCache[testId] = name
    }

    private fun flushToDatabase() {
        if (participantsCache.isNotEmpty()) {
            transaction {
                participantsCache.forEach { (testId, name) ->

                }
            }
            participantsCache.clear()
        }
    }
}

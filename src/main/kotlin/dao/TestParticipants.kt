package dao

import dao.BoardQuesOptions.entityId
import dao.TestParticipantsTesting.clientDefault
import dao.TestParticipantsTesting.uniqueIndex
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.*

object TestParticipants : IdTable<String>("test_participants") {
    val boardTestId = reference("board_test_id", BoardTests)
    val userId = varchar("user_id", 255)
    val isJoined = bool("is_joined").default(false)
    val hasAttempted = bool("has_attempted").default(false)
    val joinedAt = datetime("joined_at").clientDefault { LocalDateTime.now() }.nullable()
    override val id = TestParticipantsTesting.varchar("id", 36).clientDefault { UUID.randomUUID().toString() }.entityId()
    override val primaryKey = PrimaryKey(id)
}


object TestParticipantsTesting : Table("test_participants_testing") {
    val userId = varchar("user_id", 255)
    val isJoined = bool("is_joined").default(false)
    val hasAttempted = bool("has_attempted").default(false)
    val joinedAt = datetime("joined_at").clientDefault { LocalDateTime.now() }.nullable()
    private val id = varchar("id", 36).clientDefault { UUID.randomUUID().toString() }.uniqueIndex()
    override val primaryKey = PrimaryKey(id)
}


fun addTestParticipant(boardTestId: UUID, userId: String) {
    transaction {
        TestParticipantsTesting.insert {
            it[TestParticipantsTesting.userId] = userId
            it[isJoined] = true
            it[joinedAt] = LocalDateTime.now()
        }
    }
}

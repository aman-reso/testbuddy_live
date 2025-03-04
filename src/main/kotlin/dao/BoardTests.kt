package dao

import dao.BoardQuesOptions.entityId
import dao.TestParticipantsTesting.clientDefault
import dao.TestParticipantsTesting.uniqueIndex
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.util.*

object BoardTests : IdTable<String>("board_tests") {
    override val id = varchar("id", 36).clientDefault { UUID.randomUUID().toString() }.entityId()
    val title = varchar("title", 255)
    val subunitId = reference("subunit_id", SubUnits).nullable()
    val chapterId = reference("chapter_id", Chapters).nullable()
    val testType = enumerationByName("test_type", 20, TestType::class)
    val createdBy = varchar("created_by", 255).nullable()
    val createdAt = datetime("created_at").clientDefault { java.time.LocalDateTime.now() }
    val updatedAt = datetime("updated_at").clientDefault { java.time.LocalDateTime.now() }
    val startTime = datetime("start_time")
    val endTime = datetime("end_time")
    override val primaryKey = PrimaryKey(id)
}

enum class TestType {
    PRACTICE, LIVE, ARCHIVE
}

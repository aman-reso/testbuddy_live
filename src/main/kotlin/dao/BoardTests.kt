package dao

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.datetime

object BoardTests : UUIDTable("board_tests") {
    val title = varchar("title", 255)
    val subunitId = reference("subunit_id", SubUnits).nullable()
    val chapterId = reference("chapter_id", Chapters).nullable()
    val testType = enumerationByName("test_type", 20, TestType::class)
    val createdBy = varchar("created_by", 255).nullable()
    val createdAt = datetime("created_at").clientDefault { java.time.LocalDateTime.now() }
    val updatedAt = datetime("updated_at").clientDefault { java.time.LocalDateTime.now() }
    val startTime = datetime("start_time")
    val endTime = datetime("end_time")
}

enum class TestType {
    PRACTICE, LIVE, ARCHIVE
}

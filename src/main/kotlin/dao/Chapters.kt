package dao

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.datetime

object Chapters : UUIDTable("board_chapter") {
    val chapterName = varchar("chapter_name", 255).uniqueIndex()
    val subjectId = reference("subject_id", Subjects)
    val createdAt = datetime("created_at").clientDefault { java.time.LocalDateTime.now() }
    val updatedAt = datetime("updated_at").clientDefault { java.time.LocalDateTime.now() }
}

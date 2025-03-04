package dao

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.datetime

object Books : UUIDTable("board_books") {
    val bookName = varchar("book_name", 255)
    val fileUrl = varchar("file_url", 500)
    val format = enumerationByName("format", 10, BookFormat::class)
    val subjectId = reference("subject_id", Subjects)
    val chapterId = reference("chapter_id", Chapters).nullable()
    val createdAt = datetime("created_at").clientDefault { java.time.LocalDateTime.now() }
    val updatedAt = datetime("updated_at").clientDefault { java.time.LocalDateTime.now() }
}

enum class BookFormat {
    PDF, HTML
}

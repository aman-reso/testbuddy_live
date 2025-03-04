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

object Chapters : IdTable<String>("board_chapter") {
    val chapterName = varchar("chapter_name", 255).uniqueIndex()
    val subjectId = reference("subject_id", Subjects)
    val createdAt = datetime("created_at").clientDefault { java.time.LocalDateTime.now() }
    val updatedAt = datetime("updated_at").clientDefault { java.time.LocalDateTime.now() }
    override val id = varchar("id", 36).clientDefault { UUID.randomUUID().toString() }.entityId()
    override val primaryKey = PrimaryKey(id)
}

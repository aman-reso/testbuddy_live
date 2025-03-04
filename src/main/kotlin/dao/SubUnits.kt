package dao

import dao.BoardQuesOptions.entityId
import dao.TestParticipantsTesting.clientDefault
import dao.TestParticipantsTesting.uniqueIndex
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime.now
import java.util.*

object SubUnits : IdTable<String>("board_subunit") {
    val subunitName = varchar("sub_unit_name", 255).uniqueIndex()
    val chapterId = reference("chapter_id", Chapters)
    val createdAt = datetime("created_at").clientDefault { now() }
    val updatedAt = datetime("updated_at").clientDefault { now() }
    override val id = varchar("id", 36).clientDefault { UUID.randomUUID().toString() }.entityId()
    override val primaryKey = PrimaryKey(id)
}

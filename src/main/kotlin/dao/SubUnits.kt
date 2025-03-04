package dao

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime.now

object SubUnits : UUIDTable("board_subunit") {
    val subunitName = varchar("sub_unit_name", 255).uniqueIndex()
    val chapterId = reference("chapter_id", Chapters)
    val createdAt = datetime("created_at").clientDefault { now() }
    val updatedAt = datetime("updated_at").clientDefault { now() }
}

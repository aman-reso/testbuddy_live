package dao

import dao.TestParticipantsTesting.clientDefault
import dao.TestParticipantsTesting.uniqueIndex
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table
import java.util.*

object BoardQuesOptions : IdTable<String>("board_ques_options") {
    override val id = varchar("id", 36).clientDefault { UUID.randomUUID().toString() }.entityId() // ✅ Must be public
    val optionTextEn = text("option_text_en")
    val optionTextHn = text("option_text_hn").nullable()
    val prompt = varchar("prompt", 10)
    val questionId = reference("question_id", BoardQuestions)
    override val primaryKey = PrimaryKey(id)
}

package dao

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable

object BoardQuesOptions : UUIDTable("board_ques_options") {
    val optionTextEn = text("option_text_en")
    val optionTextHn = text("option_text_hn").nullable()
    val prompt = varchar("prompt", 10)
    val questionId = reference("question_id", BoardQuestions)
}

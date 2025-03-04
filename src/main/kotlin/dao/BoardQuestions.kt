package dao

import dao.BoardQuesOptions.entityId
import dao.TestParticipantsTesting.clientDefault
import dao.TestParticipantsTesting.uniqueIndex
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table
import java.util.*


object BoardQuestions : IdTable<String>("board_question") {
    val questionTextEn = text("question_text_en").nullable()
    val questionTextHn = text("question_text_hn").nullable()
    val questionType = enumerationByName("question_type", 20, QuestionType::class)
    val positiveMarks = float("positive_marks").default(1.0f)
    val negativeMarks = float("negative_marks").default(0.0f)
    val correctOption = varchar("correct_option", 10)
    val boardTestId = reference("board_test_id", BoardTests)
    override val id = varchar("id", 36).clientDefault { UUID.randomUUID().toString() }.entityId()
    override val primaryKey = PrimaryKey(id)
}

enum class QuestionType {
    MCQ, DESCRIPTIVE
}

package dao

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable


object BoardQuestions : UUIDTable("board_question") {
    val questionTextEn = text("question_text_en").nullable()
    val questionTextHn = text("question_text_hn").nullable()
    val questionType = enumerationByName("question_type", 20, QuestionType::class)
    val positiveMarks = float("positive_marks").default(1.0f)
    val negativeMarks = float("negative_marks").default(0.0f)
    val correctOption = varchar("correct_option", 10)
    val boardTestId = reference("board_test_id", BoardTests)
}

enum class QuestionType {
    MCQ, DESCRIPTIVE
}

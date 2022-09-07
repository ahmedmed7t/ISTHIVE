package com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire

internal data class SkipLogic(
    val ID: Int,
    val QuestionGUID: String,
    val QChoiceGUID: String,
    val MinValue: Int,
    val MaxValue: Int,
    val SkipToQuestionGUID: String
)
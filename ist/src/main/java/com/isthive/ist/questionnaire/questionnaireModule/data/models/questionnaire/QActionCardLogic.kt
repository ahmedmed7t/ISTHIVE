package com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire

internal data class QActionCardLogic(
    val MaxValue: Int,
    val MinValue: Int,
    val QChoiceGUID: String,
    val QuestionActionCard: QuestionActionCard,
    val SurveyQuestionGUID: String
)
package com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire

internal data class QuestionActionCard(
    val ImageUrl: String,
    val QuestionActionCardButtons: List<QuestionActionCardButton>,
    val SubTitle: String,
    val Title: String
)
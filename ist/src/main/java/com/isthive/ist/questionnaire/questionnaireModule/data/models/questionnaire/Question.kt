package com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire

internal data class Question(
    val Choices: List<Choice>,
    val IsRequired: Boolean,
    val QuestionGUID: String,
    val QuestionID: Int,
    val QuestionType: QuestionType,
    val Scale: Int,
    val StarOption: StarOption?,
    val TemplateID: String,
    val Title: String
)
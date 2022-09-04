package com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire

import java.util.ArrayList

internal data class Question(
    val Choices: ArrayList<Choice>?,
    val IsRequired: Boolean,
    val QuestionGUID: String,
    val QuestionID: Int,
    val QuestionType: QuestionType,
    val Scale: Int,
    val StarOption: StarOption?,
    val TemplateID: String,
    val Title: String
)
package com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire

internal data class Choice(
    val ChoiceGUID: String,
    val ChoiceID: Int,
    val ImageURL: String,
    val QuestionGUID: String,
    val Title: String,
    val Type: ChoiceType,
    var isSelected: Boolean = false
)
package com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire

internal data class WelcomeMessage(
    val Mode: Int,
    val SubTitle: String,
    val SubTitleStyle: SubTitleStyleXX,
    val TemplateID: Any,
    val Title: String,
    val TitleStyle: TitleStyleXX
)
package com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire

internal data class ThankyouMessage(
    val Mode: Int,
    val SubTitle: String,
    val SubTitleStyle: SubTitleStyleX,
    val TemplateID: String,
    val Title: String,
    val TitleStyle: TitleStyleX
)
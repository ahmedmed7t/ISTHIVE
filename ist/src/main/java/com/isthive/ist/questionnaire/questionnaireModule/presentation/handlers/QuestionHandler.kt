package com.isthive.ist.questionnaire.questionnaireModule.presentation.handlers

internal interface QuestionHandler {
    fun onNextClicked()
    fun onBackClicked()
    fun onSubmitClicked()
    fun onCloseClicked()
}
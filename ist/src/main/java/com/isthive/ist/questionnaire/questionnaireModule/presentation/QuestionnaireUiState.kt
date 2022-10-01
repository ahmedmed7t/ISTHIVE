package com.isthive.ist.questionnaire.questionnaireModule.presentation

import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Survey

internal sealed class QuestionnaireUiState{
    internal data class LoadSurveySuccess(val survey: Survey): QuestionnaireUiState()
    internal data class SaveSurveySuccess(val message: String): QuestionnaireUiState()
}
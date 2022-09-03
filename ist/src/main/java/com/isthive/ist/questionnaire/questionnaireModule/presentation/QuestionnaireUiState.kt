package com.isthive.ist.questionnaire.questionnaireModule.presentation

import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Survey

internal sealed class QuestionnaireUiState{
    internal data class Success(val survey: Survey): QuestionnaireUiState()
}
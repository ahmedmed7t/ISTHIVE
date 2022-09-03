package com.isthive.ist.questionnaire.questionnaireModule.data.models

import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Survey

internal data class GetQuestionnaireResponse(
    val IsSuccess: Boolean,
    val Message: String,
    val StatusCode: Int,
    val Survey: Survey
)
package com.isthive.ist.questionnaire.questionnaireModule.data.models

internal data class GenerateTokenRequest(
    val GrantType: String,
    val Password: String,
    val RefreshToken: String = "",
    val UserName: String
)
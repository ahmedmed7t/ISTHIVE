package com.isthive.ist.questionnaire.questionnaireModule.domain.models

internal data class TokenModel(
    val accessToken: String,
    val refreshToken: String,
    val userName: String
)

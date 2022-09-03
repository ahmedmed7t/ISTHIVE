package com.isthive.ist.questionnaire.questionnaireModule.data.models

import com.google.gson.annotations.SerializedName

internal data class GenerateTokenResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("token_type")
    val tokenType: String
)
package com.isthive.ist.questionnaire.questionnaireModule.domain.repository

import com.isthive.ist.questionnaire.questionnaireModule.data.models.*
import com.isthive.ist.questionnaire.questionnaireModule.domain.models.TokenModel
import retrofit2.Response

internal interface QuestionnaireRepository {
    suspend fun getToken(getTokenRequest: GenerateTokenRequest): Response<GenerateTokenResponse>
    suspend fun getQuestionnaire(
        accessToken: String,
        getQuestionnaireRequest: GetQuestionnaireRequest
    ): Response<GetQuestionnaireResponse>

    fun saveTokenData(tokenModel: TokenModel)
    fun getAccessToken(): String
    suspend fun saveSurvey(
        accessToken: String,
        saveSurveyRequest: SaveSurveyRequest
    ): Response<SaveSurveyResponse>
}
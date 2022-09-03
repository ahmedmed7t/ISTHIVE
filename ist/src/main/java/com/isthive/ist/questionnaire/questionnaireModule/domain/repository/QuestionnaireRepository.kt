package com.isthive.ist.questionnaire.questionnaireModule.domain.repository

import com.isthive.ist.questionnaire.questionnaireModule.data.models.GenerateTokenRequest
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GenerateTokenResponse
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GetQuestionnaireRequest
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GetQuestionnaireResponse
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
}
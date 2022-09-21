package com.isthive.ist.questionnaire.questionnaireModule.data.remoteDataSource

import com.isthive.ist.questionnaire.questionnaireModule.data.models.*
import retrofit2.Response

internal interface QuestionnaireRemoteDataSource {
    suspend fun getToken(getTokenRequest: GenerateTokenRequest): Response<GenerateTokenResponse>
    suspend fun getQuestionnaire(
        accessToken: String,
        getQuestionnaireRequest: GetQuestionnaireRequest
    ): Response<GetQuestionnaireResponse>

    suspend fun saveSurvey(
        accessToken: String,
        saveSurveyRequest: SaveSurveyRequest
    ): Response<GetQuestionnaireResponse>
}
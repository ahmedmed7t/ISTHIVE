package com.isthive.ist.questionnaire.questionnaireModule.data.remoteDataSource

import com.isthive.ist.app.api.ApiService
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GenerateTokenRequest
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GenerateTokenResponse
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GetQuestionnaireRequest
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GetQuestionnaireResponse
import retrofit2.Response
import javax.inject.Inject

internal class QuestionnaireRemoteDataSourceImp @Inject constructor(
    private val apiService: ApiService
): QuestionnaireRemoteDataSource {
    override suspend fun getToken(getTokenRequest: GenerateTokenRequest): Response<GenerateTokenResponse> {
        return apiService.generateToken(getTokenRequest)
    }

    override suspend fun getQuestionnaire(
        accessToken: String,
        getQuestionnaireRequest: GetQuestionnaireRequest
    ): Response<GetQuestionnaireResponse> {
        return apiService.getQuestionnaire(
            "bearer $accessToken", getQuestionnaireRequest
        )
    }
}
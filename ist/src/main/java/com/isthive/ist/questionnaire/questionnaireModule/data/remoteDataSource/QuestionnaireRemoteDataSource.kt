package com.isthive.ist.questionnaire.questionnaireModule.data.remoteDataSource

import com.isthive.ist.questionnaire.questionnaireModule.data.models.GenerateTokenRequest
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GenerateTokenResponse
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GetQuestionnaireRequest
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GetQuestionnaireResponse
import retrofit2.Response

internal interface QuestionnaireRemoteDataSource {
    suspend fun getToken(getTokenRequest: GenerateTokenRequest): Response<GenerateTokenResponse>
    suspend fun getQuestionnaire(
        accessToken: String,
        getQuestionnaireRequest: GetQuestionnaireRequest
    ): Response<GetQuestionnaireResponse>
}
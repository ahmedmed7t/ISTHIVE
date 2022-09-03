package com.isthive.ist.questionnaire.questionnaireModule.data.repository

import com.isthive.ist.questionnaire.questionnaireModule.data.localDataSource.QuestionnaireLocalDataSource
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GenerateTokenRequest
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GenerateTokenResponse
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GetQuestionnaireRequest
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GetQuestionnaireResponse
import com.isthive.ist.questionnaire.questionnaireModule.data.remoteDataSource.QuestionnaireRemoteDataSource
import com.isthive.ist.questionnaire.questionnaireModule.domain.models.TokenModel
import com.isthive.ist.questionnaire.questionnaireModule.domain.repository.QuestionnaireRepository
import retrofit2.Response
import javax.inject.Inject

internal class QuestionnaireRepositoryImp @Inject constructor(
    private val questionnaireRemoteDataSource: QuestionnaireRemoteDataSource,
    private val questionnaireLocalDataSource: QuestionnaireLocalDataSource
): QuestionnaireRepository {
    override suspend fun getToken(getTokenRequest: GenerateTokenRequest): Response<GenerateTokenResponse> {
        return questionnaireRemoteDataSource.getToken(getTokenRequest)
    }

    override suspend fun getQuestionnaire(
        accessToken: String,
        getQuestionnaireRequest: GetQuestionnaireRequest
    ): Response<GetQuestionnaireResponse> {
        return questionnaireRemoteDataSource.getQuestionnaire(
            accessToken, getQuestionnaireRequest
        )
    }

    override fun saveTokenData(tokenModel: TokenModel) {
        questionnaireLocalDataSource.saveTokenData(tokenModel)
    }

    override fun getAccessToken() =
        questionnaireLocalDataSource.getAccessToken()
}
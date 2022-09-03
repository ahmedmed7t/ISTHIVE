package com.isthive.ist.questionnaire.questionnaireModule.data.localDataSource

import com.isthive.ist.questionnaire.questionnaireModule.domain.models.TokenModel

internal interface QuestionnaireLocalDataSource {
    fun saveTokenData(tokenModel: TokenModel)
    fun getAccessToken(): String
}
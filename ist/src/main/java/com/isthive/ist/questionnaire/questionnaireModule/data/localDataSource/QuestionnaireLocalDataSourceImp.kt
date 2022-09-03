package com.isthive.ist.questionnaire.questionnaireModule.data.localDataSource

import android.content.SharedPreferences
import com.isthive.ist.app.models.Constants
import com.isthive.ist.questionnaire.questionnaireModule.domain.models.TokenModel
import javax.inject.Inject

internal class QuestionnaireLocalDataSourceImp @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : QuestionnaireLocalDataSource {

    override fun saveTokenData(tokenModel: TokenModel) {
        sharedPreferences.edit()
            .putString(Constants.ACCESS_TOKEN_KEY, tokenModel.accessToken)
            .putString(Constants.REFRESH_TOKEN_KEY, tokenModel.refreshToken)
            .putString(Constants.USER_NAME_KEY, tokenModel.userName)
            .apply()
    }

    override fun getAccessToken() =
        sharedPreferences.getString(Constants.ACCESS_TOKEN_KEY, "") ?: ""
}
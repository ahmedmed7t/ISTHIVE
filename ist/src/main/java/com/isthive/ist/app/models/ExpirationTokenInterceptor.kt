package com.isthive.ist.app.models

import android.content.SharedPreferences
import com.google.gson.Gson
import com.isthive.ist.BuildConfig
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GenerateTokenRequest
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GenerateTokenResponse
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

internal class ExpirationTokenInterceptor @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : Interceptor {

    private val gson = Gson()

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        return if (originalResponse.code == 403) {
            handle403Responses(chain, originalResponse)
        } else {
            originalResponse
        }
    }

    private fun handle403Responses(chain: Interceptor.Chain, originalResponse: Response): Response {
        originalResponse.close()
        val refreshResponse: Response = refreshAccessToken(chain)
        return if (refreshResponse.isSuccessful) {
            chain.proceed(chain.request())
        } else {
            refreshResponse
        }
    }

    private fun refreshAccessToken(chain: Interceptor.Chain): Response {
        val refreshTokenRequest: Request = Request.Builder()
            .url(BuildConfig.BASE_URL + "/api/Authentication/GenerateToken")
            .post(
                gson.toJson(
                    GenerateTokenRequest("refresh_token", "", getRefreshToken(), getUserName())
                ).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            )
            .addHeader("Content-Type", "application/json")
            .addHeader("Connection", "keep-alive")
            .build()
        val response: Response = chain.proceed(refreshTokenRequest)
        if (response.isSuccessful) {
            val refreshTokenResponse: GenerateTokenResponse =
                gson.fromJson(response.body?.string(), GenerateTokenResponse::class.java)
            // save model to shared
            saveAccessToken(refreshTokenResponse.accessToken)
            saveRefreshToken(refreshTokenResponse.refreshToken)
            response.close()
        }

        return response
    }

    private fun saveAccessToken(accessToken: String) =
        sharedPreferences.edit().putString(Constants.ACCESS_TOKEN_KEY, accessToken).apply()

    private fun saveRefreshToken(refreshToken: String) =
        sharedPreferences.edit().putString(Constants.REFRESH_TOKEN_KEY, refreshToken).apply()

    private fun getRefreshToken() =
        sharedPreferences.getString(Constants.REFRESH_TOKEN_KEY, "") ?: ""

    private fun getUserName() =
        sharedPreferences.getString(Constants.USER_NAME_KEY, "") ?: ""
}
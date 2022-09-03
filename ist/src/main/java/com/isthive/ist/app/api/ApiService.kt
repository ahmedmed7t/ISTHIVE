package com.isthive.ist.app.api

import com.isthive.ist.questionnaire.questionnaireModule.data.models.GenerateTokenRequest
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GenerateTokenResponse
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GetQuestionnaireRequest
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GetQuestionnaireResponse
import retrofit2.Response
import retrofit2.http.*

internal interface ApiService {

    @Headers("Accept: application/json")
    @POST("/api/Authentication/GenerateToken")
    suspend fun generateToken(
        @Body generateTokenRequest: GenerateTokenRequest
    ): Response<GenerateTokenResponse>

    @Headers("Accept: application/json")
    @POST("/api/Survey/GetRelevantWebSurvey")
    suspend fun getQuestionnaire(
        @Header("Authorization") accessToken: String,
        @Body getQuestionnaireRequest: GetQuestionnaireRequest
    ): Response<GetQuestionnaireResponse>

//    @Headers("Accept: application/json")
//    @GET("/LoginCLient")
//    suspend fun login(
//        @Query("PhoneNumber", encoded = false)
//        phoneNumber: String,
//        @Query("CountryCode", encoded = false)
//        countryCode: String,
//        @Query("Password", encoded = false)
//        password: String,
//    ): Response<BaseResponse<LoginResponse>>
//
//    @Headers("Accept: application/json")
//    @PUT("/SendOTP")
//    suspend fun sendOtp(
//        @Query("PhoneNumber", encoded = false)
//        phoneNumber: String,
//        @Query("CountryCode", encoded = false)
//        countryCode: String,
//        @Query("Type", encoded = false)
//        type: Int,
//    ): Response<BaseResponse<SendOtpResponse>>
}
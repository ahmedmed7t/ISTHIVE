package com.isthive.ist.app.api

import com.isthive.ist.questionnaire.questionnaireModule.data.models.*
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GenerateTokenRequest
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GenerateTokenResponse
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GetQuestionnaireRequest
import com.isthive.ist.questionnaire.questionnaireModule.data.models.GetQuestionnaireResponse
import com.isthive.ist.questionnaire.questionnaireModule.data.models.SaveSurveyRequest
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


    @Headers("Accept: application/json")
    @POST("/api/CustomerSurvey/SaveWebSurveyResponse")
    suspend fun saveSurvey(
        @Header("Authorization") accessToken: String,
        @Body saveSurveyRequest: SaveSurveyRequest
    ): Response<GetQuestionnaireResponse>

}
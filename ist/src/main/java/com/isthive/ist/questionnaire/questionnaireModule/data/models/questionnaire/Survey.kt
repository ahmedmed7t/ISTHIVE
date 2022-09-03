package com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire

internal data class Survey(
    val InvitationGuid: String,
    val Name: String,
    val QActionCardLogics: List<QActionCardLogic>,
    val Questions: List<Question>,
    val RateButtonOption: RateButtonOption,
    val SkipLogics: List<Any>,
    val SurveyGuid: String,
    val SurveyID: Int,
    val SurveyLanguage: Int,
    val SurveyOptions: SurveyOptions,
    val Title: String
)
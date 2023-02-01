package com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire

internal data class Survey(
    val InvitationGuid: String,
    val Name: String,
    val QActionCardLogics: List<QActionCardLogic>,
    val Questions: ArrayList<Question>,
    val RateButtonOption: RateButtonOption,
    val SkipLogics: ArrayList<SkipLogic>?,
    val SurveyGuid: String,
    val SurveyID: Int,
    val SurveyLanguage: String,
    val SurveyOptions: SurveyOptions,
    val Title: String
)
package com.isthive.ist.questionnaire.questionnaireModule.data.models

import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer

internal data class SaveSurveyRequest(
    var InvitationGuid: String = "",
    var SurveyGuid: String = "",
    var SurveyID: Int = 0,
    var QuestionResponses: ArrayList<Answer?> = arrayListOf()
)
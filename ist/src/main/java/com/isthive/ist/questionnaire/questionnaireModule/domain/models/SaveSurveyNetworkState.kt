package com.isthive.ist.questionnaire.questionnaireModule.domain.models

import com.isthive.ist.questionnaire.questionnaireModule.data.models.SaveSurveyResponse

internal sealed class SaveSurveyNetworkState {
    data class NetworkSuccess(val saveSurveyResponse: SaveSurveyResponse?) :
        SaveSurveyNetworkState()

    data class NetworkFail(val message: String?) : SaveSurveyNetworkState()
}

package com.isthive.ist.questionnaire.questionnaireModule.domain.models

import com.isthive.ist.questionnaire.questionnaireModule.data.models.GetQuestionnaireResponse

internal sealed class GetQuestionnaireNetworkState {
    data class NetworkSuccess(val getQuestionnaireResponse: GetQuestionnaireResponse?) :
        GetQuestionnaireNetworkState()

    data class NetworkFail(val message: String?) : GetQuestionnaireNetworkState()
}

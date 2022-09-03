package com.isthive.ist.questionnaire.questionnaireModule.domain.models

import com.isthive.ist.questionnaire.questionnaireModule.data.models.GenerateTokenResponse

internal sealed class GetTokenNetworkState{
    internal data class NetworkSuccess(val generateTokenResponse: GenerateTokenResponse?): GetTokenNetworkState()
    internal data class NetworkFail(val message: String?): GetTokenNetworkState()
}

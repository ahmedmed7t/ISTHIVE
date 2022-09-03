package com.isthive.ist.questionnaire.questionnaireModule.domain.useCases

import com.isthive.ist.questionnaire.questionnaireModule.data.models.GenerateTokenRequest
import com.isthive.ist.questionnaire.questionnaireModule.domain.models.GetTokenNetworkState
import com.isthive.ist.questionnaire.questionnaireModule.domain.repository.QuestionnaireRepository
import javax.inject.Inject

internal class GetTokenUseCase @Inject constructor(
    private val questionnaireRepository: QuestionnaireRepository
) {
    suspend operator fun invoke(userName: String, password: String): GetTokenNetworkState {
        questionnaireRepository.getToken(GenerateTokenRequest(
            GrantType = "password",
            Password = password,
            UserName = userName,
            RefreshToken = ""
        )).let {
            return if(it.isSuccessful){
                GetTokenNetworkState.NetworkSuccess(it.body())
            }else{
                GetTokenNetworkState.NetworkFail(it.message() ?: "")
            }
        }
    }
}
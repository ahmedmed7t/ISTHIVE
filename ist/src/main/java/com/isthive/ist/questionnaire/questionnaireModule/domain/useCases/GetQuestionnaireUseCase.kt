package com.isthive.ist.questionnaire.questionnaireModule.domain.useCases

import com.isthive.ist.questionnaire.questionnaireModule.data.models.GetQuestionnaireRequest
import com.isthive.ist.questionnaire.questionnaireModule.domain.models.GetQuestionnaireNetworkState
import com.isthive.ist.questionnaire.questionnaireModule.domain.repository.QuestionnaireRepository
import javax.inject.Inject

internal class GetQuestionnaireUseCase @Inject constructor(
    private val questionnaireRepository: QuestionnaireRepository
) {
    suspend operator fun invoke(accessToken: String): GetQuestionnaireNetworkState {
        questionnaireRepository.getQuestionnaire(
            accessToken, GetQuestionnaireRequest()
        ).let {
            return if(it.isSuccessful){
                GetQuestionnaireNetworkState.NetworkSuccess(it.body())
            }else{
                GetQuestionnaireNetworkState.NetworkFail(it.message())
            }
        }
    }
}
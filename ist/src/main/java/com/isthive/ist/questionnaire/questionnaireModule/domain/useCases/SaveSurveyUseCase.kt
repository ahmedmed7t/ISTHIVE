package com.isthive.ist.questionnaire.questionnaireModule.domain.useCases

import com.isthive.ist.questionnaire.questionnaireModule.data.models.SaveSurveyRequest
import com.isthive.ist.questionnaire.questionnaireModule.domain.models.GetQuestionnaireNetworkState
import com.isthive.ist.questionnaire.questionnaireModule.domain.models.SaveSurveyNetworkState
import com.isthive.ist.questionnaire.questionnaireModule.domain.repository.QuestionnaireRepository
import javax.inject.Inject

internal class SaveSurveyUseCase @Inject constructor(
    private val questionnaireRepository: QuestionnaireRepository
) {
    suspend operator fun invoke(
        accessToken: String,
        saveSurveyRequest: SaveSurveyRequest
    ): SaveSurveyNetworkState {
        questionnaireRepository.saveSurvey(
            accessToken, saveSurveyRequest
        ).let {
            return if (it.isSuccessful && it.body()?.IsSuccess == true) {
                SaveSurveyNetworkState.NetworkSuccess(it.body())
            } else {
                SaveSurveyNetworkState.NetworkFail(it.message())
            }
        }
    }
}
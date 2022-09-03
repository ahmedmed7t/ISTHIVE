package com.isthive.ist.questionnaire.questionnaireModule.domain.useCases

import com.isthive.ist.questionnaire.questionnaireModule.domain.models.TokenModel
import com.isthive.ist.questionnaire.questionnaireModule.domain.repository.QuestionnaireRepository
import javax.inject.Inject

internal class SaveTokenDataUseCase @Inject constructor(
    private val questionnaireRepository: QuestionnaireRepository
)  {
    operator fun invoke(userName: String, accessToken: String, refreshToken: String){
        questionnaireRepository.saveTokenData(TokenModel(
            accessToken, refreshToken, userName
        ))
    }
}
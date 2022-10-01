package com.isthive.ist.questionnaire.questionnaireModule.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isthive.ist.questionnaire.questionnaireModule.data.models.SaveSurveyRequest
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionnaireModule.domain.models.GetQuestionnaireNetworkState
import com.isthive.ist.questionnaire.questionnaireModule.domain.models.GetTokenNetworkState
import com.isthive.ist.questionnaire.questionnaireModule.domain.models.SaveSurveyNetworkState
import com.isthive.ist.questionnaire.questionnaireModule.domain.useCases.GetQuestionnaireUseCase
import com.isthive.ist.questionnaire.questionnaireModule.domain.useCases.GetTokenUseCase
import com.isthive.ist.questionnaire.questionnaireModule.domain.useCases.SaveSurveyUseCase
import com.isthive.ist.questionnaire.questionnaireModule.domain.useCases.SaveTokenDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class QuestionnaireViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val saveTokenDataUseCase: SaveTokenDataUseCase,
    private val getQuestionnaireUseCase: GetQuestionnaireUseCase,
    private val saveSurveyUseCase: SaveSurveyUseCase,
) : ViewModel() {

    private val _questionnaireState = MutableLiveData<QuestionnaireUiState>()
    val questionnaireState: LiveData<QuestionnaireUiState>
        get() = _questionnaireState

    val questions = MutableLiveData<ArrayList<Question>>()

    val saveSurveyRequest = SaveSurveyRequest()
    private var accessToken = ""

    fun generateToken(userName: String, password: String) {
        viewModelScope.launch {
            getTokenUseCase(userName, password).let {
                when (it) {
                    is GetTokenNetworkState.NetworkFail -> {
                        Log.v("Medhat", "getting token fail ${it.message}")
                    }
                    is GetTokenNetworkState.NetworkSuccess -> {
                        it.generateTokenResponse?.let { response ->
                            saveTokenDataUseCase(
                                userName = userName,
                                accessToken = response.accessToken,
                                refreshToken = response.refreshToken
                            )
                            loadQuestionnaire(response.accessToken)
                            accessToken = response.accessToken
                        }
                    }
                }
            }
        }
    }

    private suspend fun loadQuestionnaire(accessToken: String) {
        getQuestionnaireUseCase(accessToken).let {
            when (it){
                is GetQuestionnaireNetworkState.NetworkFail ->{}
                is GetQuestionnaireNetworkState.NetworkSuccess -> {
                    _questionnaireState.value = it.getQuestionnaireResponse?.Survey?.let { it1 ->
                        saveSurveyRequest.InvitationGuid = it1.InvitationGuid
                        saveSurveyRequest.SurveyGuid = it1.SurveyGuid
                        saveSurveyRequest.SurveyID = it1.SurveyID

                        questions.value = it1.Questions
                        QuestionnaireUiState.LoadSurveySuccess(it1)
                    }
                }
            }
        }
    }

    fun saveSurvey(){
        viewModelScope.launch {
            saveSurveyUseCase.invoke(accessToken, saveSurveyRequest).let {
                when(it){
                    is SaveSurveyNetworkState.NetworkFail -> {}
                    is SaveSurveyNetworkState.NetworkSuccess -> {
                        QuestionnaireUiState.SaveSurveySuccess(it.saveSurveyResponse?.Message ?: "")
                    }
                }
            }
        }
    }
}
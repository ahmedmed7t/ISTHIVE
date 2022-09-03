package com.isthive.ist.questionnaire.questionnaireModule.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isthive.ist.questionnaire.questionnaireModule.domain.models.GetQuestionnaireNetworkState
import com.isthive.ist.questionnaire.questionnaireModule.domain.models.GetTokenNetworkState
import com.isthive.ist.questionnaire.questionnaireModule.domain.useCases.GetQuestionnaireUseCase
import com.isthive.ist.questionnaire.questionnaireModule.domain.useCases.GetTokenUseCase
import com.isthive.ist.questionnaire.questionnaireModule.domain.useCases.SaveTokenDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class QuestionnaireViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val saveTokenDataUseCase: SaveTokenDataUseCase,
    private val getQuestionnaireUseCase: GetQuestionnaireUseCase
) : ViewModel() {

    private val _questionnaireState = MutableLiveData<QuestionnaireUiState>()
    val questionnaireState: LiveData<QuestionnaireUiState>
        get() = _questionnaireState

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
                        }
                    }
                }
            }
        }
    }

    private suspend fun loadQuestionnaire(accessToken: String) {
        getQuestionnaireUseCase(accessToken).let {
            when (it){
                is GetQuestionnaireNetworkState.NetworkFail ->
                    Log.v(
                        "Medhat", "getting Questionnaire fail "
                    )
                is GetQuestionnaireNetworkState.NetworkSuccess -> {
                    Log.v("Medhat", "view Model success")
                    _questionnaireState.value = it.getQuestionnaireResponse?.Survey?.let { it1 ->
                        Log.v("Medhat", "view Model success not null")
                        QuestionnaireUiState.Success(
                            it1
                        )
                    }
                }
            }
        }
    }
}
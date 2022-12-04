package com.isthive.ist.questionnaire.questionnaireModule.presentation

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.isthive.ist.R
import com.isthive.ist.questionnaire.provider.QuestionProvider
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.DisplayMode
import com.isthive.ist.questionnaire.questionnaireModule.presentation.adapter.FullScreenListAdapter
import com.isthive.ist.questionnaire.questionnaireModule.presentation.handlers.QuestionHandler
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView
import com.isthive.ist.questionnaire.viewContainers.BottomSheetContainer
import com.isthive.ist.questionnaire.viewContainers.ContainersContract
import com.isthive.ist.questionnaire.viewContainers.PopupContainerView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
internal class QuestionnaireActivity : AppCompatActivity(), QuestionHandler {
    private val viewModel: QuestionnaireViewModel by viewModels()

    private lateinit var questionProvider: QuestionProvider
    private lateinit var loading: ProgressBar
    private var containerView: ContainersContract? = null
    private var currentView: BaseQuestionView? = null

    private var questionViews = Stack<BaseQuestionView>()
    private val answers: HashMap<String?, Answer?> = hashMapOf()

    private lateinit var fullScreenRecyclerView: RecyclerView
    private val fullScreenListAdapter: FullScreenListAdapter by lazy {
        FullScreenListAdapter(arrayListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionare)

        fullScreenRecyclerView = findViewById(R.id.fullScreenRecyclerView)
        loading = findViewById(R.id.surveyProgress)
        loading.visibility = View.VISIBLE
        listenToViewModelValues()
        viewModel.generateToken("InAppUser", "InApp2021")
    }

    private fun listenToViewModelValues() {
        viewModel.questionnaireState.observe(this) {
            when (it) {
                is QuestionnaireUiState.LoadSurveySuccess -> {
                    loading.visibility = View.GONE
                    questionProvider = QuestionProvider(it.survey.Questions, it.survey.SkipLogics)
                    currentView = questionProvider.getNextQuestion(null, this)
                    currentView?.let { questionView ->
                        questionViews.push(questionView)
                        when (it.survey.SurveyOptions.DisplayMode) {
                            DisplayMode.BottomCard -> {
                                displayFullScreenView(it)
//                                displayBottomSheetView(it, questionView)
                            }
                            DisplayMode.Popup -> {
                                displayFullScreenView(it)
//                                displayDialogView(it, questionView)
                            }
                            DisplayMode.FullScreen -> {
                                displayFullScreenView(it)

//                                containerView = BottomSheetContainer()
//                                    .mainView(questionView)
//                                    .isSingleQuestion(it.survey.Questions.size == 1)
//                                    .setNavigationMode(it.survey.SurveyOptions.NavigationMode)
//                                    .setWelcomeMessage(it.survey.SurveyOptions.Theme.WelcomeMessage)
//                                    .setHandler(this)
//                                (containerView as BottomSheetContainer).show(
//                                    supportFragmentManager,
//                                    "tag"
//                                )
                            }
                        }
                    }
                }
                is QuestionnaireUiState.SaveSurveySuccess -> {
                    finishCallApiState(it.message)
                }
                is QuestionnaireUiState.SaveSurveyFail -> {
                    finishCallApiState(it.message)
                }
            }
        }
    }

    override fun onNextClicked() {
        if (currentView?.question?.IsRequired == true) {
            if (currentView?.isAnswerValid == true) {
                answers[currentView?.question?.QuestionGUID] = currentView?.getAnswer()
                goToNextStep()
            } else {
                currentView?.showError()
            }
        } else {
            if (currentView?.getAnswer() != null)
                answers[currentView?.question?.QuestionGUID] = currentView?.getAnswer()
            goToNextStep()
        }
    }

    private fun goToNextStep() {
        if (questionViews.size < viewModel.questions.value?.size!!) {
            val nextView = questionProvider.getNextQuestion(currentView?.getAnswer(), this)
            nextView?.let {
                containerView?.addView(
                    it,
                    isFirstItem = questionProvider.isFirstQuestion(nextView.question),
                    isLastItem = questionProvider.isLastQuestion(nextView.question)
                )
                questionViews.push(it)
                currentView = it
            }
        }
    }

    override fun onBackClicked() {
        if (questionViews.size > 1) {
            questionViews.pop()
            questionViews.lastElement()
            containerView?.addView(
                questionViews.lastElement(),
                isFirstItem = questionProvider.isFirstQuestion(questionViews.lastElement().question),
                isLastItem = questionProvider.isLastQuestion(questionViews.lastElement().question)
            )
            currentView = questionViews.lastElement()
            questionProvider.updateQuestionIndex((currentView as BaseQuestionView).question)
        }
    }

    override fun onSubmitClicked() {
        if (currentView?.question?.IsRequired == true) {
            if (currentView?.isAnswerValid == true) {
                answers[currentView?.question?.QuestionGUID] = currentView?.getAnswer()
                submitSurvey()
            } else {
                currentView?.showError()
            }
        } else {
            if (currentView?.getAnswer() != null)
                answers[currentView?.question?.QuestionGUID] = currentView?.getAnswer()
            submitSurvey()
        }
    }

    private fun submitSurvey() {
        if (answers.isNotEmpty()) {
            for (item in answers)
                viewModel.saveSurveyRequest.QuestionResponses.add(item.value)
        }
        loading.visibility = View.VISIBLE
        viewModel.saveSurvey()
    }

    override fun onDismiss() {
        onBackPressed()
    }

    private fun displayDialogView(
        questionnaireUiState: QuestionnaireUiState.LoadSurveySuccess,
        questionView: View
    ) {
        containerView = PopupContainerView()
            .mainView(questionView)
            .isSingleQuestion(questionnaireUiState.survey.Questions.size == 1)
            .setNavigationMode(questionnaireUiState.survey.SurveyOptions.NavigationMode)
            .setWelcomeMessage(questionnaireUiState.survey.SurveyOptions.Theme.WelcomeMessage)
            .setHandler(this)
        (containerView as PopupContainerView).show(
            supportFragmentManager,
            "tag"
        )
    }

    private fun displayBottomSheetView(
        questionnaireUiState: QuestionnaireUiState.LoadSurveySuccess,
        questionView: View
    ) {
        containerView = BottomSheetContainer()
            .mainView(questionView)
            .isSingleQuestion(questionnaireUiState.survey.Questions.size == 1)
            .setNavigationMode(questionnaireUiState.survey.SurveyOptions.NavigationMode)
            .setWelcomeMessage(questionnaireUiState.survey.SurveyOptions.Theme.WelcomeMessage)
            .setHandler(this)
        (containerView as BottomSheetContainer).show(
            supportFragmentManager,
            "tag"
        )
    }

    private fun displayFullScreenView(questionnaireUiState: QuestionnaireUiState.LoadSurveySuccess){
        val questionViews = arrayListOf<BaseQuestionView>()
        for (item in questionnaireUiState.survey.Questions)
            questionViews.add(questionProvider.loadViewRelevantToQuestion(item, context = this))

        fullScreenListAdapter.updateQuestionsList(questionViews)
        fullScreenRecyclerView.adapter = fullScreenListAdapter
    }

    private fun finishCallApiState(message:String){
        loading.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        containerView?.dismissContainer()
    }

    internal companion object {
        const val QUESTIONNAIRE_USER_NAME = "QUESTIONNAIRE_USER_NAME"
        const val QUESTIONNAIRE_PASSWORD = "QUESTIONNAIRE_PASSWORD"
    }
}
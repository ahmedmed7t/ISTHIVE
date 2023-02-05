package com.isthive.ist.questionnaire.questionnaireModule.presentation

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.isthive.ist.R
import com.isthive.ist.app.helper.LocaleHelper
import com.isthive.ist.questionnaire.provider.FullScreenQuestionProvider
import com.isthive.ist.questionnaire.provider.QuestionProvider
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.*
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionnaireModule.presentation.adapter.FullScreenListAdapter
import com.isthive.ist.questionnaire.questionnaireModule.presentation.handlers.AnswerHandler
import com.isthive.ist.questionnaire.questionnaireModule.presentation.handlers.QuestionHandler
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView
import com.isthive.ist.questionnaire.viewContainers.BottomSheetContainer
import com.isthive.ist.questionnaire.viewContainers.ContainersContract
import com.isthive.ist.questionnaire.viewContainers.PopupContainerView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
internal class QuestionnaireActivity : AppCompatActivity(), QuestionHandler, AnswerHandler {
    private val viewModel: QuestionnaireViewModel by viewModels()

    private var questionProvider: QuestionProvider? = null
    private lateinit var fullScreenProvider: FullScreenQuestionProvider
    private lateinit var loading: ProgressBar
    private var containerView: ContainersContract? = null
    private var currentView: BaseQuestionView? = null

    private var questionViews = Stack<BaseQuestionView>()
    private val answers: HashMap<String?, Answer?> = hashMapOf()

    private lateinit var welcomeContainer: LinearLayout
    private lateinit var welcomeTitle: TextView
    private lateinit var welcomeDescription: TextView
    private lateinit var takeSurveyButton: TextView

    private lateinit var fullScreenRecyclerView: RecyclerView
    private lateinit var fullScreenSubmitButton: TextView
    private lateinit var fullScreenLayout: ConstraintLayout
    private val fullScreenListAdapter: FullScreenListAdapter by lazy {
        FullScreenListAdapter(arrayListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionare)

        welcomeContainer = findViewById(R.id.fullScreenWelcomeContainer)
        welcomeTitle = findViewById(R.id.fullScreenWelcomeTitle)
        welcomeDescription = findViewById(R.id.fullScreenWelcomeMessage)
        takeSurveyButton = findViewById(R.id.fullScreenWelcomeTakeSurvey)

        fullScreenRecyclerView = findViewById(R.id.fullScreenRecyclerView)
        fullScreenSubmitButton = findViewById(R.id.fullScreenSubmitButton)
        fullScreenLayout = findViewById(R.id.fullScreenListContainer)
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
                    when (it.survey.SurveyOptions.DisplayMode) {
                        DisplayMode.BottomCard -> {
                            questionProvider =
                                QuestionProvider(it.survey.Questions, it.survey.SkipLogics)
                            currentView = questionProvider?.getNextQuestion(null, this)
                            currentView?.let { questionView ->
                                questionViews.push(questionView)
                                displayBottomSheetView(it, questionView)
                            }
                        }
                        DisplayMode.Popup -> {
                            fullScreenLayout.visibility = View.VISIBLE
                            fullScreenProvider = FullScreenQuestionProvider(
                                it.survey.Questions,
                                it.survey.SkipLogics
                            )
                            displayFullScreenView(it)
//                            questionProvider =
//                                QuestionProvider(it.survey.Questions, it.survey.SkipLogics)
//                            currentView = questionProvider?.getNextQuestion(null, this)
//                            currentView?.let { questionView ->
//                                questionViews.push(questionView)
//                                displayDialogView(it, questionView)
//                            }
                        }
                        DisplayMode.FullScreen -> {
                            fullScreenLayout.visibility = View.VISIBLE
                            fullScreenProvider = FullScreenQuestionProvider(
                                it.survey.Questions,
                                it.survey.SkipLogics
                            )
                            displayFullScreenView(it)
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
            if (currentView?.canGoNext == true) {
                if (currentView?.getAnswer() != null)
                    answers[currentView?.question?.QuestionGUID] = currentView?.getAnswer()
                goToNextStep()
            }
        }
    }

    private fun goToNextStep() {
        if (questionViews.size < viewModel.questions.value?.size!!) {
            val nextView = questionProvider?.getNextQuestion(currentView?.getAnswer(), this)
            nextView?.let {
                containerView?.addView(
                    it,
                    isFirstItem = questionProvider?.isFirstQuestion(nextView.question) ?: false,
                    isLastItem = questionProvider?.isLastQuestion(nextView.question) ?: false
                )
                questionViews.push(it)
                currentView = it
            }
            containerView?.updateProgressBar(questionProvider?.getProgressPercentage() ?: 0)
        }
    }

    override fun onBackClicked() {
        if (questionViews.size > 1) {
            questionViews.pop()
            questionViews.lastElement()
            containerView?.addView(
                questionViews.lastElement(),
                isFirstItem = questionProvider?.isFirstQuestion(questionViews.lastElement().question)
                    ?: false,
                isLastItem = questionProvider?.isLastQuestion(questionViews.lastElement().question)
                    ?: false
            )
            currentView = questionViews.lastElement()
            questionProvider?.updateQuestionIndex((currentView as BaseQuestionView).question)
            containerView?.updateProgressBar(questionProvider?.getProgressPercentage() ?: 0)
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

    override fun onAnswerClicked(answer: Answer, question: Question?) {
        question?.let {
            val nextQuestions = fullScreenProvider.getNextListOfQuestions(answer, question, this)
            nextQuestions.last().answerHandler = this
            fullScreenListAdapter.appendQuestions(nextQuestions, question)
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
            .setHasCloseButton(questionnaireUiState.survey.SurveyOptions.EnableCloseButton)
            .setHasProgressBar(questionnaireUiState.survey.SurveyOptions.HasProgressBar)
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
            .setHasCloseButton(questionnaireUiState.survey.SurveyOptions.EnableCloseButton)
            .setHasProgressBar(questionnaireUiState.survey.SurveyOptions.HasProgressBar)
            .setHandler(this)
        (containerView as BottomSheetContainer).show(
            supportFragmentManager,
            "tag"
        )
    }

    private fun displayFullScreenView(
        questionnaireUiState: QuestionnaireUiState.LoadSurveySuccess
    ) {
        val questionList = fullScreenProvider.getInitialList(this)
        questionList.last().answerHandler = this

        fullScreenListAdapter.updateQuestionsList(questionList)
        fullScreenRecyclerView.adapter = fullScreenListAdapter

        fullScreenSubmitButton.setOnClickListener {
            if (fullScreenListAdapter.checkAnswersAvailability()) {
                val fullScreenAnswers = fullScreenListAdapter.collectAnswers()
                if (fullScreenAnswers.isNotEmpty()) {
                    viewModel.saveSurveyRequest.QuestionResponses.addAll(fullScreenAnswers)
                }
                loading.visibility = View.VISIBLE
                viewModel.saveSurvey()
            }
        }

        questionnaireUiState.survey.SurveyOptions.Theme.WelcomeMessage?.let {
            when (it.Mode) {
                WelcomeMode.Separate_View -> {
                    viewWelcomeMessage(it.Title, it.SubTitle)
                }
                WelcomeMode.First_Question -> {
                    Handler(Looper.getMainLooper()).postDelayed({
                        fullScreenListAdapter.showWelcomeMessageForFirstItem(it.Title)
                    }, 50)
                }
                else -> {
                    hideWelcomeMessage()
                }
            }
        }
    }

    private fun viewWelcomeMessage(title: String, description: String) {
        welcomeTitle.text = title
        welcomeDescription.text = description
        welcomeContainer.visibility = View.VISIBLE
        fullScreenLayout.visibility = View.GONE
        takeSurveyButton.setOnClickListener {
            hideWelcomeMessage()
        }
    }

    private fun hideWelcomeMessage() {
        welcomeContainer.visibility = View.GONE
        fullScreenLayout.visibility = View.VISIBLE
    }

    private fun finishCallApiState(message: String) {
        loading.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        fullScreenLayout.visibility = View.GONE
        containerView?.dismissContainer()
    }

    internal companion object {
        const val QUESTIONNAIRE_USER_NAME = "QUESTIONNAIRE_USER_NAME"
        const val QUESTIONNAIRE_PASSWORD = "QUESTIONNAIRE_PASSWORD"
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }
}
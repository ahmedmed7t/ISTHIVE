package com.isthive.ist.questionnaire.questionnaireModule.presentation

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.isthive.ist.R
import com.isthive.ist.questionnaire.provider.QuestionProvider
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.NavigationMode
import com.isthive.ist.questionnaire.questionnaireModule.presentation.handlers.QuestionHandler
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView
import com.isthive.ist.questionnaire.viewContainers.BottomSheetContainer
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@AndroidEntryPoint
internal class QuestionnaireActivity : AppCompatActivity(), QuestionHandler {
    private val viewModel: QuestionnaireViewModel by viewModels()

    private lateinit var questionProvider: QuestionProvider
    private lateinit var button: TextView
    private lateinit var bottomSheetContainer: BottomSheetContainer
    private var currentView: BaseQuestionView? = null

    private var questionViews = Stack<BaseQuestionView>()
    private val answers: HashMap<String?, Answer?> = hashMapOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionare)

        button = findViewById(R.id.getToken)
        listenToViewModelValues()
        button.setOnClickListener {
            viewModel.generateToken("InAppUser", "InApp2021")
        }
    }

    private fun listenToViewModelValues() {
        viewModel.questionnaireState.observe(this) {
            when (it) {
                is QuestionnaireUiState.Success -> {
                    questionProvider = QuestionProvider(it.survey.Questions, it.survey.SkipLogics)
                    currentView = questionProvider.getNextQuestion(null, this)
                    currentView?.let { questionView ->
                        questionViews.push(questionView)
                        bottomSheetContainer = BottomSheetContainer()
                            .mainView(questionView)
                            .setNavigationMode(it.survey.SurveyOptions.NavigationMode)
                            .setWelcomeMessage(it.survey.SurveyOptions.Theme.WelcomeMessage)
                            .setHandler(this)
                        bottomSheetContainer.show(supportFragmentManager, "tag")
                    }
                }
            }
        }
    }

    internal companion object {
        const val QUESTIONNAIRE_USER_NAME = "QUESTIONNAIRE_USER_NAME"
        const val QUESTIONNAIRE_PASSWORD = "QUESTIONNAIRE_PASSWORD"
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
                bottomSheetContainer.addView(
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
            bottomSheetContainer.addView(
                questionViews.lastElement(),
                isFirstItem = questionProvider.isFirstQuestion(questionViews.lastElement().question),
                isLastItem = questionProvider.isLastQuestion(questionViews.lastElement().question)
            )
            currentView = questionViews.lastElement()
            questionProvider.updateQuestionIndex((currentView as BaseQuestionView).question)
        }
    }

    override fun onSubmitClicked() {

    }

    override fun onCloseClicked() {

    }
}
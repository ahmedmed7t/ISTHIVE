package com.isthive.ist.questionnaire.questionnaireModule.presentation

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.isthive.ist.R
import com.isthive.ist.questionnaire.provider.QuestionProvider
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.QuestionType
import com.isthive.ist.questionnaire.questionnaireModule.presentation.handlers.QuestionHandler
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView
import com.isthive.ist.questionnaire.questionsViews.fcr.FCRQuestion
import com.isthive.ist.questionnaire.viewContainers.BottomSheetContainer
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
internal class QuestionnaireActivity : AppCompatActivity(), QuestionHandler {
    private val viewModel: QuestionnaireViewModel by viewModels()

    private lateinit var questionProvider: QuestionProvider
    private lateinit var button: TextView
    private lateinit var bottomSheetContainer: BottomSheetContainer
    private var currentView: BaseQuestionView? = null

    private var questionViews = Stack<BaseQuestionView>()


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
                    currentView?.let {
                        questionViews.push(it)
                        bottomSheetContainer = BottomSheetContainer()
                            .mainView(it)
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
        Log.v("Medhat", "question stack on next size is ${questionViews.size}")
        if (questionViews.size < viewModel.questions.value?.size!!) {
            val nextView = questionProvider.getNextQuestion(currentView?.getAnswer(), this)
            Log.v("Medhat", "next question is ${nextView}")
            nextView?.let {
                bottomSheetContainer.addView(it)
                questionViews.push(it)
                currentView = it
            }
        }
    }

    override fun onBackClicked() {
        if (questionViews.size > 1) {
            questionViews.pop()
            Log.v("Medhat", "question stack on back size is ${questionViews.size}")
            Log.v("Medhat", "last question is ${questionViews.lastElement()}")
            questionViews.lastElement()
            bottomSheetContainer.addView(questionViews.lastElement())
            currentView = questionViews.lastElement()
            questionProvider.updateQuestionIndex((currentView as BaseQuestionView).question)
        }
    }

    override fun onSubmitClicked() {

    }

    override fun onCloseClicked() {

    }
}
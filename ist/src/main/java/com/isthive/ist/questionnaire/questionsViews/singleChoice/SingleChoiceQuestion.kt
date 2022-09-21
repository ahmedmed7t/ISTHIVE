package com.isthive.ist.questionnaire.questionsViews.singleChoice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.doOnAttach
import androidx.recyclerview.widget.RecyclerView
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.AnswerChoice
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView

internal class SingleChoiceQuestion internal constructor(
    context: Context,
    question: Question?,
    resourceFile: Int = R.layout.single_choice_question
) :
    BaseQuestionView(context, question, resourceFile), SingleChoiceAdapterHandler {

    private val MODEREN_STYLE = "Modern"

    private lateinit var questionTitle: TextView
    private lateinit var choicesRecyclerView: RecyclerView
    private lateinit var questionRequired: TextView
    private lateinit var errorMessage: TextView

    private lateinit var choicesAdapter: SingleChoiceAdapter
    private var selectedChoice: Int? = null

    override fun initViews(view: View?) {
        view?.apply {
            questionTitle = findViewById(R.id.singleChoiceQuestionTitle)
            questionRequired = findViewById(R.id.singleChoiceQuestionRequired)
            questionDescription = findViewById(R.id.singleChoiceQuestionDescription)
            choicesRecyclerView = findViewById(R.id.singleChoiceQuestionRecyclerView)
            errorMessage = findViewById(R.id.singleChoiceQuestionErrorMessage)
            choicesRecyclerView.setHasFixedSize(true)
            choicesRecyclerView.isNestedScrollingEnabled = false
        }
    }

    override fun viewQuestionData() {
        question?.apply {
            questionTitle.text = Title
            if(IsRequired)
                questionRequired.visibility = View.VISIBLE
            else
                questionRequired.visibility = View.GONE
            Choices?.let { choices ->
                choicesAdapter = SingleChoiceAdapter(choices, TemplateID == MODEREN_STYLE, this@SingleChoiceQuestion)
                choicesRecyclerView.adapter = choicesAdapter
            }
        }
    }

    override fun handleUiEvents() {

    }

    override fun showError() {
        isAnswerValid = false
        errorMessage.visibility = View.VISIBLE
    }

    override fun getAnswer(): Answer? {
        question?.apply {
            selectedChoice?.let { choice ->
                return Answer(
                    QuestionGUID, QuestionID, arrayListOf(
                        AnswerChoice(
                            Choices!![choice].ChoiceGUID,
                            Choices[choice].ChoiceID,
                            choicesAdapter.otherText.ifBlank { null }
                        )
                    ), null
                )
            }
        }
        return null
    }

    override fun onChoiceSelected(position: Int) {
        isAnswerValid = true
        errorMessage.visibility = View.GONE
        selectedChoice = position
    }
}
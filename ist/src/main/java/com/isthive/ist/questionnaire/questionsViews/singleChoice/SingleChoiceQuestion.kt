package com.isthive.ist.questionnaire.questionsViews.singleChoice

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.AnswerChoice
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView

internal class SingleChoiceQuestion internal constructor(
    context: Context,
    question: Question,
    resourceFile: Int = R.layout.single_choice_question
) :
    BaseQuestionView(context, question, resourceFile), SingleChoiceAdapterHandler {

    private val MODEREN_STYLE = "Modern"

    private var questionTitle: TextView? = null
    private var choicesRecyclerView: RecyclerView? = null
    private var questionRequired: TextView? = null
    private var errorMessage: TextView? = null

    private lateinit var choicesAdapter: SingleChoiceAdapter
    private var selectedChoice: Int? = null

    override fun initViews(view: View?) {
        view?.apply {
            questionTitle = findViewById(R.id.singleChoiceQuestionTitle)
            questionRequired = findViewById(R.id.singleChoiceQuestionRequired)
            questionDescription = findViewById(R.id.singleChoiceQuestionDescription)
            choicesRecyclerView = findViewById(R.id.singleChoiceQuestionRecyclerView)
            errorMessage = findViewById(R.id.singleChoiceQuestionErrorMessage)
            choicesRecyclerView?.setHasFixedSize(true)
            choicesRecyclerView?.isNestedScrollingEnabled = false
        }
    }

    override fun viewQuestionData() {
        question.apply {
            questionTitle?.text = Title
            if (IsRequired)
                questionRequired?.visibility = View.VISIBLE
            else
                questionRequired?.visibility = View.GONE
            Choices?.let { choices ->
                choicesAdapter = SingleChoiceAdapter(
                    choices,
                    TemplateID == MODEREN_STYLE,
                    this@SingleChoiceQuestion
                )
                choicesRecyclerView?.adapter = choicesAdapter
            }
        }
    }

    override fun handleUiEvents() {

    }

    override fun showError() {
        isAnswerValid = false
        errorMessage?.visibility = View.VISIBLE
    }

    override fun getAnswer(): Answer {
        val answer = Answer(
            question.QuestionGUID, question.QuestionID, NumberResponse = null
        )
        selectedChoice?.let { choice ->
            answer.SelectedChoices = arrayListOf(
                AnswerChoice(
                    question.Choices?.get(choice)?.ChoiceGUID ?: "",
                    question.Choices?.get(choice)?.ChoiceID ?: 0,
                    choicesAdapter.otherText.ifBlank { null }
                )
            )
        }
        return answer
    }

    override fun onChoiceSelected(position: Int) {
        isAnswerValid = true
        errorMessage?.visibility = View.GONE
        selectedChoice = position

        question.apply {
            selectedChoice?.let { choice ->
                answerHandler?.onAnswerClicked(Answer(
                    QuestionGUID, QuestionID, arrayListOf(
                        AnswerChoice(
                            Choices!![choice].ChoiceGUID,
                            Choices[choice].ChoiceID,
                            choicesAdapter.otherText.ifBlank { null }
                        )
                    ), null
                ), question)
            }
        }
    }
}
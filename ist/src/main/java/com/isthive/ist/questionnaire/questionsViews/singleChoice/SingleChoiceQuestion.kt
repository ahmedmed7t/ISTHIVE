package com.isthive.ist.questionnaire.questionsViews.singleChoice

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.AnswerChoice
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.ChoiceType
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
    private var errorMessage: TextView? = null

    private lateinit var choicesAdapter: SingleChoiceAdapter
    private var selectedChoice: Int? = null

    override var isAnswerValid: Boolean = false
        get() {
            return checkValidation()
        }

    override fun initViews(view: View?) {
        view?.apply {
            questionTitle = findViewById(R.id.singleChoiceQuestionTitle)
            questionDescription = findViewById(R.id.singleChoiceQuestionDescription)
            choicesRecyclerView = findViewById(R.id.singleChoiceQuestionRecyclerView)
            errorMessage = findViewById(R.id.singleChoiceQuestionErrorMessage)
            choicesRecyclerView?.setHasFixedSize(true)
            choicesRecyclerView?.isNestedScrollingEnabled = false
            choicesRecyclerView?.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    val action = e.action
                    when (action) {
                        MotionEvent.ACTION_MOVE -> rv.parent.requestDisallowInterceptTouchEvent(true)
                    }
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })
        }
    }

    override fun viewQuestionData() {
        question.apply {
            questionTitle?.setText(
                getSpannableTitle(Title, IsRequired),
                TextView.BufferType.SPANNABLE
            )

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
//        errorMessage?.visibility = View.VISIBLE
    }

    override fun getAnswer(): Answer? {
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
        return getValidAnswer(answer)
    }

    private fun checkValidation(): Boolean {
        selectedChoice?.let {
            if (selectedChoice == -1)
                return false
            return !(question.Choices!![selectedChoice!!].Type == ChoiceType.Other_Choice && choicesAdapter.otherText.isBlank())
        }
        return false
    }

    override fun onChoiceSelected(position: Int) {
        isAnswerValid = true
        errorMessage?.visibility = View.GONE
        selectedChoice = position

        question.apply {
            selectedChoice?.let { choice ->
                if (Choices!![choice].Type == ChoiceType.Other_Choice && choicesAdapter.otherText.isBlank())
                    isAnswerValid = false

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
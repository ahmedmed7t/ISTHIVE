package com.isthive.ist.questionnaire.questionsViews.multipleChoice

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.AnswerChoice
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView


internal class MultipleChoiceQuestion internal constructor(
    context: Context,
    question: Question,
    resourceFile: Int = R.layout.multiple_choice_question
) :
    BaseQuestionView(context, question, resourceFile), MultipleChoiceAdapterHandler {

    private val MODERN_STYLE = "Modern"

    private var questionTitle: TextView? = null
    private var choicesRecyclerView: RecyclerView? = null
    private var errorMessage: TextView? = null
    override var isAnswerValid: Boolean = false
        get() {
            return checkAnswerValidation()
        }

//    private var nested: ScrollView? = null

    private lateinit var choicesAdapter: MultipleChoiceAdapter
    private val selectedItems = arrayListOf<Int>()

    override fun initViews(view: View?) {
        view?.apply {
            questionTitle = findViewById(R.id.MultipleChoiceQuestionTitle)
            questionDescription = findViewById(R.id.MultipleChoiceQuestionDescription)
            choicesRecyclerView = findViewById(R.id.MultipleChoiceQuestionRecyclerView)
            errorMessage = findViewById(R.id.MultipleChoiceQuestionErrorMessage)
            choicesRecyclerView?.setHasFixedSize(true)

            choicesRecyclerView?.addOnItemTouchListener(object : OnItemTouchListener {
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
            questionTitle?.text = Title
            questionTitle?.setText(
                getSpannableTitle(Title, IsRequired),
                TextView.BufferType.SPANNABLE
            )

            Choices?.let { choices ->
                choicesAdapter = MultipleChoiceAdapter(
                    choices,
                    TemplateID == MODERN_STYLE,
                    this@MultipleChoiceQuestion
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

    private fun hideError() {
        isAnswerValid = true
//        errorMessage?.visibility = View.GONE
    }

    override fun getAnswer(): Answer? {
        val selectedChoices = arrayListOf<AnswerChoice>()
        question.apply {
            for (item in selectedItems) {
                if (item == choicesAdapter.otherIndex) {
                    selectedChoices.add(
                        AnswerChoice(
                            Choices!![item].ChoiceGUID,
                            Choices[item].ChoiceID,
                            choicesAdapter.otherText.ifBlank { null }
                        )
                    )
                } else {
                    selectedChoices.add(
                        AnswerChoice(
                            Choices!![item].ChoiceGUID,
                            Choices[item].ChoiceID,
                            null
                        )
                    )
                }
            }

            val answer = if (selectedChoices.isNotEmpty())
                Answer(
                    QuestionGUID, QuestionID, selectedChoices, null
                )
            else
                Answer(
                    QuestionGUID, QuestionID, null, null
                )
            return getValidAnswer(answer)
        }
    }

    private fun checkAnswerValidation(): Boolean {
        if (selectedItems.isEmpty())
            return false

        for (item in selectedItems) {
            if (item == choicesAdapter.otherIndex && choicesAdapter.otherText.isBlank()) {
                return false
            }
        }
        return true
    }

    override fun onChoiceSelected(position: Int) {
        hideError()
        selectedItems.add(position)
    }

    override fun onChoiceUnSelected(position: Int) {
        selectedItems.remove(position)
        if (selectedItems.isEmpty() && question.IsRequired) {
            showError()
        } else {
            hideError()
        }
    }
}
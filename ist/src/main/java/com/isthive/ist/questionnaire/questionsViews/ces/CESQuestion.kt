package com.isthive.ist.questionnaire.questionsViews.ces

import android.content.Context
import android.view.View
import android.widget.TextView
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.AnswerChoice
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Choice
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView
import com.isthive.ist.questionnaire.questionsViews.RadioButtonListItem

internal class CESQuestion internal constructor(
    context: Context,
    question: Question,
    resourceLayout: Int = R.layout.ces_question
) : BaseQuestionView(context, question, resourceLayout) {

    private var questionTitle: TextView? = null
    private var errorMessage: TextView? = null

    private var option1: RadioButtonListItem? = null
    private var option2: RadioButtonListItem? = null
    private var option3: RadioButtonListItem? = null
    private var option4: RadioButtonListItem? = null
    private var option5: RadioButtonListItem? = null
    private var option6: RadioButtonListItem? = null
    private var option7: RadioButtonListItem? = null

    private var lastSelectedItem: RadioButtonListItem? = null
    private var lastSelectedIndex: Int? = null

    override fun initViews(view: View?) {
        view?.apply {
            questionTitle = findViewById(R.id.cesQuestionTitle)
            errorMessage = findViewById(R.id.cesQuestionErrorMessage)
            questionDescription = findViewById(R.id.cesQuestionDescription)
            option1 = findViewById(R.id.cesOption1)
            option2 = findViewById(R.id.cesOption2)
            option3 = findViewById(R.id.cesOption3)
            option4 = findViewById(R.id.cesOption4)
            option5 = findViewById(R.id.cesOption5)
            option6 = findViewById(R.id.cesOption6)
            option7 = findViewById(R.id.cesOption7)
        }
    }

    override fun viewQuestionData() {
        question.apply {
            questionTitle?.text = Title
            setOptionsStyle(TemplateID)
            questionTitle?.setText(
                getSpannableTitle(Title, IsRequired),
                TextView.BufferType.SPANNABLE
            )

            when (Scale) {
                3 -> {
                    option4?.visibility = View.GONE
                    option5?.visibility = View.GONE
                    option6?.visibility = View.GONE
                    option7?.visibility = View.GONE
                }
                5 -> {
                    option6?.visibility = View.GONE
                    option7?.visibility = View.GONE
                }
            }
            Choices?.let { choices ->
                showChoices(choices)
            }
        }
    }

    private fun setOptionsStyle(style: String) {
        option1?.setMode(style)
        option2?.setMode(style)
        option3?.setMode(style)
        option4?.setMode(style)
        option5?.setMode(style)
        option6?.setMode(style)
        option7?.setMode(style)
    }

    override fun handleUiEvents() {
        option1?.setOnClickListener {
            onOptionClicked(option1, 0)
        }
        option2?.setOnClickListener {
            onOptionClicked(option2, 1)
        }
        option3?.setOnClickListener {
            onOptionClicked(option3, 2)
        }
        option4?.setOnClickListener {
            onOptionClicked(option4, 3)
        }
        option5?.setOnClickListener {
            onOptionClicked(option5, 4)
        }
        option6?.setOnClickListener {
            onOptionClicked(option6, 5)
        }
        option7?.setOnClickListener {
            onOptionClicked(option7, 6)
        }
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
        val answer = Answer(question.QuestionGUID, question.QuestionID)

        lastSelectedIndex?.let { lastIndex ->
            answer.SelectedChoices = arrayListOf(
                AnswerChoice(
                    question.Choices?.get(lastIndex)?.ChoiceGUID ?: "",
                    question.Choices?.get(lastIndex)?.ChoiceID ?: 0,
                    null
                )
            )
        }
        return getValidAnswer(answer)
    }

    private fun onOptionClicked(selectedItem: RadioButtonListItem?, index: Int) {
        hideError()
        lastSelectedIndex = index
        lastSelectedItem?.setOptionSelected(false)
        selectedItem?.setOptionSelected(true)
        lastSelectedItem = selectedItem

        lastSelectedIndex?.let { lastIndex ->
            question.apply {
                answerHandler?.onAnswerClicked(
                    Answer(
                        QuestionGUID, QuestionID, arrayListOf(
                            AnswerChoice(
                                Choices!![lastIndex].ChoiceGUID,
                                Choices[lastIndex].ChoiceID,
                                null
                            )
                        )
                    ), question
                )
            }
        }
    }

    private fun showChoices(choices: ArrayList<Choice>) {
        when (choices.size) {
            3 -> {
                option1?.setOptionTitle(choices[0].Title)
                option2?.setOptionTitle(choices[1].Title)
                option3?.setOptionTitle(choices[2].Title)
            }
            5 -> {
                option1?.setOptionTitle(choices[0].Title)
                option2?.setOptionTitle(choices[1].Title)
                option3?.setOptionTitle(choices[2].Title)
                option4?.setOptionTitle(choices[3].Title)
                option5?.setOptionTitle(choices[4].Title)
            }
            7 -> {
                option1?.setOptionTitle(choices[0].Title)
                option2?.setOptionTitle(choices[1].Title)
                option3?.setOptionTitle(choices[2].Title)
                option4?.setOptionTitle(choices[3].Title)
                option5?.setOptionTitle(choices[4].Title)
                option6?.setOptionTitle(choices[5].Title)
                option7?.setOptionTitle(choices[6].Title)
            }
        }
    }
}
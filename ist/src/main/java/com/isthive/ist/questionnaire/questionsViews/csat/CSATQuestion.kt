package com.isthive.ist.questionnaire.questionsViews.csat

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

internal class CSATQuestion internal constructor(
    context: Context,
    question: Question?,
    resourceLayout: Int = R.layout.csat_question
) : BaseQuestionView(context, question, resourceLayout) {

    private lateinit var questionTitle: TextView
    private lateinit var errorMessage: TextView

    private lateinit var option1: RadioButtonListItem
    private lateinit var option2: RadioButtonListItem
    private lateinit var option3: RadioButtonListItem
    private lateinit var option4: RadioButtonListItem
    private lateinit var option5: RadioButtonListItem
    private lateinit var option6: RadioButtonListItem
    private lateinit var option7: RadioButtonListItem
    private lateinit var questionRequired: TextView

    private var lastSelectedItem: RadioButtonListItem? = null
    private var lastSelectedIndex: Int? = null

    override fun initViews(view: View?) {
        view?.apply {
            questionTitle = findViewById(R.id.csatQuestionTitle)
            errorMessage = findViewById(R.id.csatQuestionErrorMessage)
            questionDescription = findViewById(R.id.csatQuestionDescription)
            option1 = findViewById(R.id.csatOption1)
            option2 = findViewById(R.id.csatOption2)
            option3 = findViewById(R.id.csatOption3)
            option4 = findViewById(R.id.csatOption4)
            option5 = findViewById(R.id.csatOption5)
            option6 = findViewById(R.id.csatOption6)
            option7 = findViewById(R.id.csatOption7)
            questionRequired = findViewById(R.id.csatQuestionRequired)
        }
    }

    override fun viewQuestionData() {
        question?.apply {
            questionTitle.text = Title
            setOptionsStyle(TemplateID)
            if (IsRequired)
                questionRequired.visibility = View.VISIBLE
            else
                questionRequired.visibility = View.GONE
            when (Scale) {
                2 -> {
                    option3.visibility = View.GONE
                    option4.visibility = View.GONE
                    option5.visibility = View.GONE
                    option6.visibility = View.GONE
                    option7.visibility = View.GONE
                }
                3 -> {
                    option4.visibility = View.GONE
                    option5.visibility = View.GONE
                    option6.visibility = View.GONE
                    option7.visibility = View.GONE
                }
                5 -> {
                    option6.visibility = View.GONE
                    option7.visibility = View.GONE
                }
            }
            Choices?.let { choices ->
                showChoices(choices)
            }
        }
    }

    private fun setOptionsStyle(style: String) {
        option1.setMode(style)
        option2.setMode(style)
        option3.setMode(style)
        option4.setMode(style)
        option5.setMode(style)
        option6.setMode(style)
        option7.setMode(style)
    }

    override fun handleUiEvents() {
        option1.setOnClickListener {
            onOptionClicked(option1, 0)
        }
        option2.setOnClickListener {
            onOptionClicked(option2, 1)
        }
        option3.setOnClickListener {
            onOptionClicked(option3, 2)
        }
        option4.setOnClickListener {
            onOptionClicked(option4, 3)
        }
        option5.setOnClickListener {
            onOptionClicked(option5, 4)
        }
        option6.setOnClickListener {
            onOptionClicked(option6, 5)
        }
        option7.setOnClickListener {
            onOptionClicked(option7, 6)
        }
    }

    override fun showError() {
        isAnswerValid = false
        errorMessage.visibility = View.VISIBLE
    }

    private fun hideError() {
        isAnswerValid = true
        errorMessage.visibility = View.GONE
    }

    override fun getAnswer(): Answer? {
        lastSelectedIndex?.let { lastIndex ->
            question?.apply {
                return Answer(
                    QuestionGUID, QuestionID, arrayListOf(
                        AnswerChoice(
                            Choices!![lastIndex].ChoiceGUID,
                            Choices[lastIndex].ChoiceID,
                            null
                        )
                    )
                )
            }
        }
        return null
    }

    private fun onOptionClicked(selectedItem: RadioButtonListItem, index: Int) {
        hideError()
        lastSelectedIndex = index
        lastSelectedItem?.setOptionSelected(false)
        selectedItem.setOptionSelected(true)
        lastSelectedItem = selectedItem

        lastSelectedIndex?.let { lastIndex ->
            question?.apply {
                answerHandler?.onAnswerClicked(
                    Answer(QuestionGUID, QuestionID, arrayListOf(
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
            2 -> {
                option1.setOptionTitle(choices[0].Title)
                option2.setOptionTitle(choices[1].Title)
            }
            3 -> {
                option1.setOptionTitle(choices[0].Title)
                option2.setOptionTitle(choices[1].Title)
                option3.setOptionTitle(choices[2].Title)
            }
            5 -> {
                option1.setOptionTitle(choices[0].Title)
                option2.setOptionTitle(choices[1].Title)
                option3.setOptionTitle(choices[2].Title)
                option4.setOptionTitle(choices[3].Title)
                option5.setOptionTitle(choices[4].Title)
            }
            7 -> {
                option1.setOptionTitle(choices[0].Title)
                option2.setOptionTitle(choices[1].Title)
                option3.setOptionTitle(choices[2].Title)
                option4.setOptionTitle(choices[3].Title)
                option5.setOptionTitle(choices[4].Title)
                option6.setOptionTitle(choices[5].Title)
                option7.setOptionTitle(choices[6].Title)
            }
        }
    }
}
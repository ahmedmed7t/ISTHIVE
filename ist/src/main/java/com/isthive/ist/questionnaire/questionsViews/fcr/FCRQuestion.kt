package com.isthive.ist.questionnaire.questionsViews.fcr

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.AnswerChoice
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Choice
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView
import com.isthive.ist.questionnaire.questionsViews.RadioButtonListItem

internal class FCRQuestion internal constructor(
    context: Context,
    question: Question,
    resourceLayout: Int = R.layout.fcr_question
) : BaseQuestionView(context, question, resourceLayout) {

    private var questionTitle: TextView? = null
    private var errorMessage: TextView? = null

    private var option1: RadioButtonListItem? = null
    private var option2: RadioButtonListItem? = null
    private var optionsContainer: LinearLayout? = null
    private var questionRequired: TextView? = null

    private var lastSelectedItem: RadioButtonListItem? = null
    private var lastSelectedIndex: Int? = null

    override fun initViews(view: View?) {
        view?.apply {
            questionTitle = findViewById(R.id.fcrQuestionTitle)
            questionDescription = findViewById(R.id.fcrQuestionDescription)
            errorMessage = findViewById(R.id.fcrQuestionErrorMessage)
            option1 = findViewById(R.id.fcrOption1)
            option2 = findViewById(R.id.fcrOption2)
            optionsContainer = findViewById(R.id.fcrOptionsContainer)
            questionRequired = findViewById(R.id.fcrQuestionRequired)
        }
    }

    override fun viewQuestionData() {
        question.apply {
            questionTitle?.text = Title
            setOptionsStyle(TemplateID)
            if (IsRequired)
                questionRequired?.visibility = View.VISIBLE
            else
                questionRequired?.visibility = View.GONE
            Choices?.let { choices ->
                showChoices(choices)
            }
        }
    }

    private fun setOptionsStyle(style: String) {
        option1?.setMode(style)
        option2?.setMode(style)
        if (style.contains("horizontal", ignoreCase = true)) {
            enableHorizontalMode()
        } else {
            enableVerticalMode()
        }
    }

    override fun handleUiEvents() {
        option1?.setOnClickListener {
            onOptionClicked(option1, 0)
        }
        option2?.setOnClickListener {
            onOptionClicked(option2, 1)
        }
    }

    override fun showError() {
        isAnswerValid = false
        errorMessage?.visibility = View.VISIBLE
    }

    private fun hideError() {
        isAnswerValid = true
        errorMessage?.visibility = View.GONE
    }

    override fun getAnswer(): Answer {
        val answer = Answer(question.QuestionGUID, question.QuestionID)

        lastSelectedIndex?.let { lastIndex ->
            answer.SelectedChoices = arrayListOf(
                AnswerChoice(
                    question.Choices?.get(lastIndex)?.ChoiceGUID ?: "",
                    question.Choices?.get(lastIndex)?.ChoiceID ?: 0, null
                )
            )
        }
        return answer
    }

    private fun enableHorizontalMode() {
        optionsContainer?.orientation = LinearLayout.HORIZONTAL
        changeWidthToMatchConstraints(option1)
        changeWidthToMatchConstraints(option2)
    }

    private fun enableVerticalMode() {
        optionsContainer?.orientation = LinearLayout.VERTICAL
        changeWidthToMatchParent(option1)
        changeWidthToMatchParent(option2)
    }

    private fun changeWidthToMatchParent(option: RadioButtonListItem?) {
        val param = option?.layoutParams
        param?.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        option?.layoutParams = param
    }

    private fun changeWidthToMatchConstraints(option: RadioButtonListItem?) {
        val param = option?.layoutParams
        param?.width = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
        option?.layoutParams = param
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
        option1?.setOptionTitle(choices[0].Title)
        option2?.setOptionTitle(choices[1].Title)
    }
}
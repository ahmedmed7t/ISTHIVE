package com.isthive.ist.questionnaire.questionsViews.ces

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Choice
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView
import com.isthive.ist.questionnaire.questionsViews.RadioButtonListItem

class CESQuestion internal constructor(
    context: Context,
    question: Question?,
    resourceLayout: Int = R.layout.ces_question
) : BaseQuestionView(context, question, resourceLayout) {

    private lateinit var questionTitle: TextView

    private lateinit var option1: RadioButtonListItem
    private lateinit var option2: RadioButtonListItem
    private lateinit var option3: RadioButtonListItem
    private lateinit var option4: RadioButtonListItem
    private lateinit var option5: RadioButtonListItem
    private lateinit var option6: RadioButtonListItem
    private lateinit var option7: RadioButtonListItem

    private var lastSelectedItem: RadioButtonListItem? = null

    override fun initViews(view: View?) {
        view?.apply {
            questionTitle = findViewById(R.id.cesQuestionTitle)
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
        question?.apply {
            questionTitle.text = Title
            setOptionsStyle(TemplateID)
            when (Scale) {
                3 -> {
                    option2.visibility = View.GONE
                    option3.visibility = View.GONE
                    option5.visibility = View.GONE
                    option6.visibility = View.GONE
                }
                5 -> {
                    option2.visibility = View.GONE
                    option6.visibility = View.GONE
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
            onOptionClicked(option1)
        }
        option2.setOnClickListener {
            onOptionClicked(option2)
        }
        option3.setOnClickListener {
            onOptionClicked(option3)
        }
        option4.setOnClickListener {
            onOptionClicked(option4)
        }
        option5.setOnClickListener {
            onOptionClicked(option5)
        }
        option6.setOnClickListener {
            onOptionClicked(option6)
        }
        option7.setOnClickListener {
            onOptionClicked(option7)
        }
    }

    private fun onOptionClicked(selectedItem: RadioButtonListItem) {
        lastSelectedItem?.setOptionSelected(false)
        selectedItem.setOptionSelected(true)
        lastSelectedItem = selectedItem
    }

    private fun showChoices(choices: ArrayList<Choice>) {
        when (choices.size) {
            3 -> {
                option1.setOptionTitle(choices[0].Title)
                option4.setOptionTitle(choices[1].Title)
                option7.setOptionTitle(choices[2].Title)
            }
            5 -> {
                option1.setOptionTitle(choices[0].Title)
                option3.setOptionTitle(choices[1].Title)
                option4.setOptionTitle(choices[2].Title)
                option5.setOptionTitle(choices[3].Title)
                option7.setOptionTitle(choices[4].Title)
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
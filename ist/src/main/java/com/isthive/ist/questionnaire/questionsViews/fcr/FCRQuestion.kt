package com.isthive.ist.questionnaire.questionsViews.fcr

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Choice
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView
import com.isthive.ist.questionnaire.questionsViews.RadioButtonListItem

class FCRQuestion internal constructor(
    context: Context,
    question: Question?,
    resourceLayout: Int = R.layout.fcr_question
) : BaseQuestionView(context, question, resourceLayout) {

    private lateinit var questionTitle: TextView

    private lateinit var option1: RadioButtonListItem
    private lateinit var option2: RadioButtonListItem
    private lateinit var optionsContainer: LinearLayout

    private var lastSelectedItem: RadioButtonListItem? = null

    override fun initViews(view: View?) {
        view?.apply {
            questionTitle = findViewById(R.id.fcrQuestionTitle)
            option1 = findViewById(R.id.fcrOption1)
            option2 = findViewById(R.id.fcrOption2)
            optionsContainer = findViewById(R.id.fcrOptionsContainer)
        }
    }

    override fun viewQuestionData() {
        question?.apply {
            questionTitle.text = Title
            setOptionsStyle(TemplateID)
            Choices?.let { choices ->
                showChoices(choices)
            }
        }
    }

    private fun setOptionsStyle(style: String) {
        option1.setMode(style)
        option2.setMode(style)
        enableHorizontalMode()
//        if(style.contains("horizontal", ignoreCase = true)){
//            enableHorizontalMode()
//        }else{
//            enableVerticalMode()
//        }
    }

    override fun handleUiEvents() {
        option1.setOnClickListener {
            onOptionClicked(option1)
        }
        option2.setOnClickListener {
            onOptionClicked(option2)
        }
    }

    private fun enableHorizontalMode(){
        optionsContainer.orientation = LinearLayout.HORIZONTAL
        changeWidthToMatchConstraints(option1)
        changeWidthToMatchConstraints(option2)
    }

    private fun enableVerticalMode(){
        optionsContainer.orientation = LinearLayout.VERTICAL
        changeWidthToMatchParent(option1)
        changeWidthToMatchParent(option2)
    }

    private fun changeWidthToMatchParent(option: RadioButtonListItem){
        val param = option.layoutParams
        param.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        option.layoutParams = param
    }

    private fun changeWidthToMatchConstraints(option: RadioButtonListItem){
        val param = option.layoutParams
        param.width = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
        option.layoutParams = param
    }

    private fun onOptionClicked(selectedItem: RadioButtonListItem) {
        lastSelectedItem?.setOptionSelected(false)
        selectedItem.setOptionSelected(true)
        lastSelectedItem = selectedItem
    }

    private fun showChoices(choices: ArrayList<Choice>) {
        option1.setOptionTitle(choices[0].Title)
        option2.setOptionTitle(choices[1].Title)
    }
}
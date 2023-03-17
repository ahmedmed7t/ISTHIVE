package com.isthive.ist.questionnaire.questionsViews.sliding

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.slider.Slider
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView

internal class SlidingQuestion internal constructor(
    context: Context,
    question: Question,
    resourceLayout: Int = R.layout.sliding_question
) :
    BaseQuestionView(context, question, resourceLayout) {

    private var questionTitle: TextView? = null
    private var slider: Slider? = null
    private var selectedValue: Int? = null

    private var lastSelectedNumber: TextView? = null

    private lateinit var number1: TextView
    private lateinit var number2: TextView
    private lateinit var number3: TextView
    private lateinit var number4: TextView
    private lateinit var number5: TextView

    override fun initViews(view: View?) {
        view?.apply {
            questionTitle = findViewById(R.id.slidingQuestionTitle)
            questionDescription = findViewById(R.id.slidingQuestionDescription)
            slider = findViewById(R.id.slidingQuestionSlider)
            number1 = findViewById(R.id.slidingNumber1)
            number2 = findViewById(R.id.slidingNumber2)
            number3 = findViewById(R.id.slidingNumber3)
            number4 = findViewById(R.id.slidingNumber4)
            number5 = findViewById(R.id.slidingNumber5)
        }
    }

    override fun viewQuestionData() {
        question.let {
            questionTitle?.text = it.Title
            questionTitle?.setText(
                getSpannableTitle(it.Title, it.IsRequired),
                TextView.BufferType.SPANNABLE
            )
        }
    }

    override fun handleUiEvents() {
        slider?.addOnSliderTouchListener(object : Slider.OnSliderTouchListener{
            override fun onStartTrackingTouch(slider: Slider) {}

            override fun onStopTrackingTouch(slider: Slider) {
                isAnswerValid = true
                selectedValue = slider.value.toInt()
                onNumberSelected()
                question.apply {
                    answerHandler?.onAnswerClicked(
                        Answer(
                            QuestionGUID, QuestionID, null, selectedValue, null
                        ), question
                    )
                }
            }

        })
    }

    override fun showError() {
    }

    private fun onNumberSelected() {
        lastSelectedNumber?.setTextColor(ContextCompat.getColor(context, R.color.gray))
        when (selectedValue) {
            1 -> {
                number1.setTextColor(ContextCompat.getColor(context, R.color.blue))
                lastSelectedNumber = number1
            }
            2 -> {
                number2.setTextColor(ContextCompat.getColor(context, R.color.blue))
                lastSelectedNumber = number2
            }
            3 -> {
                number3.setTextColor(ContextCompat.getColor(context, R.color.blue))
                lastSelectedNumber = number3
            }
            4 -> {
                number4.setTextColor(ContextCompat.getColor(context, R.color.blue))
                lastSelectedNumber = number4
            }
            5 -> {
                number5.setTextColor(ContextCompat.getColor(context, R.color.blue))
                lastSelectedNumber = number5
            }
        }
    }

    override fun getAnswer(): Answer? {
        question.apply {
            val answer = Answer(
                QuestionGUID, QuestionID, null, selectedValue, null
            )
            return getValidAnswer(answer)
        }
    }
}
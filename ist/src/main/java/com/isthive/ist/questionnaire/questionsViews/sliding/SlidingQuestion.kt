package com.isthive.ist.questionnaire.questionsViews.sliding

import android.content.Context
import android.view.View
import android.widget.TextView
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
    private var questionRequired: TextView? = null
    private var slider: Slider? = null

    override fun initViews(view: View?) {
        isAnswerValid = true
        view?.apply {
            questionTitle = findViewById(R.id.slidingQuestionTitle)
            questionDescription = findViewById(R.id.slidingQuestionDescription)
            questionRequired = findViewById(R.id.slidingQuestionRequired)
            slider = findViewById(R.id.slidingQuestionSlider)
        }
    }

    override fun viewQuestionData() {
        question.let {
            questionTitle?.text = it.Title
            if (it.IsRequired)
                questionRequired?.visibility = View.VISIBLE
            else
                questionRequired?.visibility = View.GONE
        }
    }

    override fun handleUiEvents() {
        slider?.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
            question.apply {
                answerHandler?.onAnswerClicked(
                    Answer(
                        QuestionGUID, QuestionID, null, value.toInt(), null
                    ), question
                )
            }
        })
    }

    override fun showError() {
    }

    override fun getAnswer(): Answer {
        question.apply {
            return Answer(
                QuestionGUID, QuestionID, null, slider?.value?.toInt(), null
            )
        }
    }
}
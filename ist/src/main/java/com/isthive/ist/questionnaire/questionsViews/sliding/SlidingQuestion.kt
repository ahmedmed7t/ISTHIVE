package com.isthive.ist.questionnaire.questionsViews.sliding

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnAttach
import com.google.android.material.slider.Slider
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView

internal class SlidingQuestion internal constructor(
    context: Context,
    question: Question?,
    resourceLayout: Int = R.layout.sliding_question
) :
    BaseQuestionView(context, question, resourceLayout) {

    private lateinit var questionTitle: TextView
    private lateinit var questionRequired: TextView
    private lateinit var slider: Slider

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
        question?.let {
            questionTitle.text = it.Title
            if(it.IsRequired)
                questionRequired.visibility = View.VISIBLE
            else
                questionRequired.visibility = View.GONE
        }
    }

    override fun handleUiEvents() {

    }

    override fun showError() {
    }

    override fun getAnswer(): Answer? {
        question?.apply {
            return Answer(
                QuestionGUID, QuestionID,null, slider.value.toInt(), null
            )
        }
        return null
    }
}
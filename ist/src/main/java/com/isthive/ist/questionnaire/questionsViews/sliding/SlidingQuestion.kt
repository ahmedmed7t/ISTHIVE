package com.isthive.ist.questionnaire.questionsViews.sliding

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnAttach
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView

class SlidingQuestion internal constructor(
    context: Context,
    question: Question?,
    resourceLayout: Int = R.layout.sliding_question
) :
    BaseQuestionView(context, question, resourceLayout) {

    private lateinit var questionTitle: TextView

    override fun initViews(view: View?) {
        view?.apply {
            questionTitle = findViewById(R.id.slidingQuestionTitle)
        }
    }

    override fun viewQuestionData() {
        question?.let {
            questionTitle.text = it.Title
        }
    }

    override fun handleUiEvents() {

    }
}
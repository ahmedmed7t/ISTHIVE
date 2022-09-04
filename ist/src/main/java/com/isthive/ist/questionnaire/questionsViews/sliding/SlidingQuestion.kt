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

class SlidingQuestion internal constructor(context: Context, question: Question?) :
    FrameLayout(context) {

    private var view: View? = null
    private var question: Question? = null

    private lateinit var questionTitle: TextView
    private lateinit var submitButton: TextView

    init {
        if (!isInEditMode) {
            doOnAttach {
                this.question = question
                init()
            }
        }
    }

    private fun init() {
        view = LayoutInflater.from(context).inflate(R.layout.sliding_question, this, true)
        initViews(view)
        viewQuestionData()
        handleUiEvents()
    }

    private fun initViews(view: View?) {
        view?.apply {
            questionTitle = findViewById(R.id.slidingQuestionTitle)
            submitButton = findViewById(R.id.slidingQuestionSubmitButton)
        }
    }

    private fun viewQuestionData() {
        question?.let {
            questionTitle.text = it.Title
        }
    }

    private fun handleUiEvents() {
        submitButton.setOnClickListener {

        }
    }
}
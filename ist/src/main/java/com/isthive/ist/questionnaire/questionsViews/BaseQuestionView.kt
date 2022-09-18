package com.isthive.ist.questionnaire.questionsViews

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.doOnAttach
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question

abstract class BaseQuestionView internal constructor(
    context: Context,
    question: Question?,
    resourceFile: Int
) :
    FrameLayout(context) {

    internal var view: View? = null
    internal var question: Question? = null
    internal var isAnswerValid = false

    init {
        this.question = question
        if (!isInEditMode) {
            doOnAttach {
                init(resourceFile)
            }
        }
    }

    private fun init(resourceFile: Int) {
        view = LayoutInflater.from(context).inflate(resourceFile, this, true)
        initViews(view)
        viewQuestionData()
        handleUiEvents()
    }

    internal abstract fun initViews(view: View?)

    internal abstract fun viewQuestionData()

    internal abstract fun handleUiEvents()

    internal abstract fun showError()

    internal abstract fun getAnswer(): Answer?
    internal fun canSkip(): Boolean {
        question?.let {
            return !it.IsRequired
        }
        return true
    }
}
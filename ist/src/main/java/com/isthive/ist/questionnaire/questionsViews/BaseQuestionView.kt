package com.isthive.ist.questionnaire.questionsViews

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.doOnAttach
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question

abstract class BaseQuestionView internal constructor(context: Context, question: Question?, resourceFile: Int) :
    FrameLayout(context) {

    internal var view: View? = null
    internal var question: Question? = null

    init {
        if (!isInEditMode) {
            doOnAttach {
                this.question = question
                init(resourceFile)
            }
        }
    }

    private fun init(resourceFile: Int){
        Log.v("Medhat", "will inflate View ")
        view = LayoutInflater.from(context).inflate(resourceFile, this, true)
        Log.v("Medhat", "View inflated")
        initViews(view)
        viewQuestionData()
        handleUiEvents()
    }

    abstract fun initViews(view: View?)

    abstract fun viewQuestionData()

    abstract fun handleUiEvents()
}
package com.isthive.ist.questionnaire.questionsViews

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnAttach
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionnaireModule.presentation.handlers.AnswerHandler

abstract class BaseQuestionView internal constructor(
    context: Context,
    question: Question,
    resourceFile: Int
) :
    FrameLayout(context) {

    internal var view: View? = null
    internal var question: Question
    internal open var isAnswerValid = false
    internal var canGoNext = true
    internal var answerHandler: AnswerHandler? = null

    protected lateinit var questionDescription: TextView

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

    internal fun showWelcomeMessage(welcomeSubtitle: String) {
        questionDescription.visibility = View.VISIBLE
        questionDescription.text = welcomeSubtitle
    }

    internal fun getSpannableTitle(title: String): Spannable{
        val requiredString = " ${context.getString(R.string.required)}"
        val text2: String = (title + requiredString)

        val spannable: Spannable = SpannableString(text2)

        spannable.setSpan(
            RelativeSizeSpan(.7f),
            title.length,
            (title + requiredString).length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.fade_gray)),
            title.length,
            (title + requiredString).length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

    internal abstract fun getAnswer(): Answer
    internal fun canSkip(): Boolean {
        question.let {
            return !it.IsRequired
        }
    }
}
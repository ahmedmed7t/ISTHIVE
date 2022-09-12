package com.isthive.ist.questionnaire.questionsViews.input

import android.content.Context
import android.view.View
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView

class InputQuestion internal constructor(
    context: Context,
    question: Question?,
    resourceLayout: Int = R.layout.input_question
) : BaseQuestionView(context, question, resourceLayout) {

    override fun initViews(view: View?) {
        TODO("Not yet implemented")
    }

    override fun viewQuestionData() {
        TODO("Not yet implemented")
    }

    override fun handleUiEvents() {
        TODO("Not yet implemented")
    }

    override fun getAnswer(): Answer? {
        TODO("Not yet implemented")
    }
}
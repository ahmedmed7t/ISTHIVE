package com.isthive.ist.questionnaire.questionsViews.multipleChoice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.doOnAttach
import androidx.recyclerview.widget.RecyclerView
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView

internal class MultipleChoiceQuestion internal constructor(context: Context, question: Question?, resourceFile: Int = R.layout.multiple_choice_question ) :
    BaseQuestionView(context, question, resourceFile) {

    private val MODERN_STYLE = "Modern"

    private lateinit var questionTitle: TextView
    private lateinit var choicesRecyclerView: RecyclerView

    private lateinit var choicesAdapter: MultipleChoiceAdapter

    override fun initViews(view: View?) {
        view?.apply {
            questionTitle = findViewById(R.id.MultipleChoiceQuestionTitle)
            choicesRecyclerView = findViewById(R.id.MultipleChoiceQuestionRecyclerView)
            choicesRecyclerView.setHasFixedSize(true)
            choicesRecyclerView.isNestedScrollingEnabled = false
        }
    }

    override fun viewQuestionData() {
        question?.apply {
            questionTitle.text = Title
            Choices?.let{ choices ->
                choicesAdapter = MultipleChoiceAdapter(choices, TemplateID == MODERN_STYLE)
                choicesRecyclerView.adapter = choicesAdapter
            }
        }
    }

    override fun handleUiEvents() {
    }
}
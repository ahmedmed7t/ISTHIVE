package com.isthive.ist.questionnaire.questionsViews.singleChoice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.doOnAttach
import androidx.recyclerview.widget.RecyclerView
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question

internal class SingleChoiceQuestion internal constructor(context: Context, question: Question?) :
    FrameLayout(context) {

    private var view: View? = null
    private var question: Question? = null
    private val MODEREN_STYLE = "Modern"

    private lateinit var questionTitle: TextView
    private lateinit var choicesRecyclerView: RecyclerView
    private lateinit var submitButton: TextView

    private lateinit var choicesAdapter: SingleChoiceAdapter

    init {
        if (!isInEditMode) {
            doOnAttach {
                this.question = question
                init()
            }
        }
    }

    private fun init() {
        view = LayoutInflater.from(context).inflate(R.layout.single_choice_question, this, true)
        initViews(view)
        initViewData()
        handleUiEvents()
    }

    private fun initViews(view: View?) {
        view?.apply {
            questionTitle = findViewById(R.id.singleChoiceQuestionTitle)
            choicesRecyclerView = findViewById(R.id.singleChoiceQuestionRecyclerView)
            submitButton = findViewById(R.id.singleChoiceQuestionSubmitButton)
            choicesRecyclerView.setHasFixedSize(true)
            choicesRecyclerView.isNestedScrollingEnabled = false
        }
    }

    private fun initViewData() {
        question?.apply {
            questionTitle.text = Title
            Choices?.let{ choices ->
                choicesAdapter = SingleChoiceAdapter(choices, TemplateID == MODEREN_STYLE)
                choicesRecyclerView.adapter = choicesAdapter
            }
        }
    }

    private fun handleUiEvents() {
        submitButton.setOnClickListener {

        }
    }
}
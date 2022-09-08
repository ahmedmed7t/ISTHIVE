package com.isthive.ist.questionnaire.questionsViews.multipleChoice

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.AnswerChoice
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView

internal class MultipleMultipleChoiceQuestion internal constructor(
    context: Context,
    question: Question?,
    resourceFile: Int = R.layout.multiple_choice_question
) :
    BaseQuestionView(context, question, resourceFile), MultipleChoiceAdapterHandler {

    private val MODERN_STYLE = "Modern"

    private lateinit var questionTitle: TextView
    private lateinit var choicesRecyclerView: RecyclerView

    private lateinit var choicesAdapter: MultipleChoiceAdapter
    private val selectedItems = arrayListOf<Int>()

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
            Choices?.let { choices ->
                choicesAdapter = MultipleChoiceAdapter(
                    choices,
                    TemplateID == MODERN_STYLE,
                    this@MultipleMultipleChoiceQuestion
                )
                choicesRecyclerView.adapter = choicesAdapter
            }
        }
    }

    override fun handleUiEvents() {
    }

    override fun getAnswer(): Answer? {
        val selectedChoices = arrayListOf<AnswerChoice>()
        question?.apply {
            for (item in selectedItems) {
                if (item == choicesAdapter.otherIndex) {
                    selectedChoices.add(
                        AnswerChoice(
                            Choices!![item].ChoiceGUID,
                            Choices[item].ChoiceID,
                            choicesAdapter.otherText.ifBlank { null }
                        )
                    )
                } else {
                    selectedChoices.add(
                        AnswerChoice(
                            Choices!![item].ChoiceGUID,
                            Choices[item].ChoiceID,
                            null
                        )
                    )
                }
            }
            return Answer(
                QuestionGUID, QuestionID, selectedChoices, null
            )
        }

        return null
    }

    override fun onChoiceSelected(position: Int) {
        selectedItems.add(position)
    }

    override fun onChoiceUnSelected(position: Int) {
        selectedItems.remove(position)
    }
}
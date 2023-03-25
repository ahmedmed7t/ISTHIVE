package com.isthive.ist.questionnaire.questionsViews.input

import android.content.Context
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.core.widget.addTextChangedListener
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.QuestionType
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView

internal class InputQuestion internal constructor(
    context: Context,
    question: Question,
    resourceLayout: Int = R.layout.input_question
) : BaseQuestionView(context, question, resourceLayout) {

    private var questionTitle: TextView? = null
    private var questionError: TextView? = null
    private var questionInput: EditText? = null

    private lateinit var questionType: QuestionType

    override fun initViews(view: View?) {
        view?.apply {
            questionTitle = findViewById(R.id.inputQuestionTitle)
            questionDescription = findViewById(R.id.inputQuestionDescription)
            questionError = findViewById(R.id.inputQuestionErrorMessage)
            questionInput = findViewById(R.id.inputQuestionEditText)
        }
    }

    override fun viewQuestionData() {
        question.apply {
            questionTitle?.text = Title
            this@InputQuestion.questionType = QuestionType
            handleInputType()
            questionTitle?.setText(
                getSpannableTitle(Title, IsRequired),
                TextView.BufferType.SPANNABLE
            )

        }
    }

    override fun handleUiEvents() {
        questionInput?.addTextChangedListener {
            if (question.IsRequired) {
                if (it.toString().isEmpty()) {
                    isAnswerValid = false
                    questionError?.visibility = View.GONE
                    canGoNext = false
                } else if (!isTextValid(it.toString().trim())) {
                    showError()
                    canGoNext = false
                } else {
                    showSuccess()
                    canGoNext = true
                }
            } else {
                canGoNext = if (it.toString().isEmpty()) {
                    showSuccess()
                    true
                } else if (!isTextValid(it.toString().trim())) {
                    showError()
                    false
                } else {
                    showSuccess()
                    true
                }
            }
        }
    }

    override fun showError() {
        isAnswerValid = false
        questionError?.visibility = View.VISIBLE
//        questionInput?.background =
//            ContextCompat.getDrawable(context, R.drawable.error_edit_text_background)
    }

    private fun showSuccess() {
        isAnswerValid = true
        questionError?.visibility = View.GONE
//        questionInput?.background =
//            ContextCompat.getDrawable(context, R.drawable.correct_edit_text_background)
    }

    override fun getAnswer(): Answer? {
        val answer = Answer(
            question.QuestionGUID, question.QuestionID, null,
            null, questionInput?.text.toString().ifBlank { null }
        )
        return getValidAnswer(answer)
    }

    private fun handleInputType() {
        questionInput?.inputType = when (questionType) {
            QuestionType.Text_input -> {
                questionError?.text = context.getString(R.string.please_enter_valid_text)
                InputType.TYPE_CLASS_TEXT
            }
            QuestionType.Number_input -> {
                questionError?.text = context.getString(R.string.please_enter_valid_number)
                InputType.TYPE_CLASS_NUMBER
            }
            QuestionType.Email_input -> {
                questionError?.text = context.getString(R.string.please_enter_valid_email)
                InputType.TYPE_CLASS_TEXT
            }
            QuestionType.Phone_number_input -> {
                questionError?.text = context.getString(R.string.please_enter_valid_telephone)
                InputType.TYPE_CLASS_PHONE
            }
            QuestionType.Postal_code_input -> {
                questionError?.text = context.getString(R.string.please_enter_valid_postal_code)
                InputType.TYPE_CLASS_NUMBER
            }
            QuestionType.URL_input -> {
                questionError?.text = context.getString(R.string.please_enter_valid_url)
                InputType.TYPE_CLASS_TEXT
            }
            else -> {
                questionError?.text = context.getString(R.string.please_enter_valid_text)
                InputType.TYPE_CLASS_TEXT
            }
        }
    }

    private fun isTextValid(text: String): Boolean {
        return when (questionType) {
            QuestionType.Text_input -> {
                text.isNotBlank()
            }
            QuestionType.Number_input -> {
                (text.isNotBlank() && text.isDigitsOnly())
            }
            QuestionType.Email_input -> {
                text.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()
            }
            QuestionType.Phone_number_input -> {
                text.isNotBlank() && text.length in 9..15
            }
            QuestionType.Postal_code_input -> {
                (text.isNotBlank() && text.isDigitsOnly() && text.length == 5)
            }
            QuestionType.URL_input -> {
                text.isNotBlank() && android.util.Patterns.WEB_URL.matcher(text).matches()
            }
            else -> {
                false
            }
        }
    }
}
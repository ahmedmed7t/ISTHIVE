package com.isthive.ist.questionnaire.questionsViews.nps

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView

internal class NPSQuestion internal constructor(
    context: Context,
    question: Question,
    resourceFile: Int = R.layout.nps_question
) :
    BaseQuestionView(context, question, resourceFile) {

    private var selectedNumber: Int? = null
    private var lastSelectedNumber: TextView? = null

    private var questionTitle: TextView? = null
    private var errorMessage: TextView? = null
    private var scale10Container: ConstraintLayout? = null
    private var scale5Container: ConstraintLayout? = null

    private var number0_10: TextView? = null
    private var number1_10: TextView? = null
    private var number2_10: TextView? = null
    private var number3_10: TextView? = null
    private var number4_10: TextView? = null
    private var number5_10: TextView? = null
    private var number6_10: TextView? = null
    private var number7_10: TextView? = null
    private var number8_10: TextView? = null
    private var number9_10: TextView? = null
    private var number10_10: TextView? = null

    private var number1_5: TextView? = null
    private var number2_5: TextView? = null
    private var number3_5: TextView? = null
    private var number4_5: TextView? = null
    private var number5_5: TextView? = null

    override fun initViews(view: View?) {
        view?.apply {
            questionTitle = findViewById(R.id.npsQuestionTitle)
            questionDescription = findViewById(R.id.npsQuestionDescription)
            errorMessage = findViewById(R.id.npsQuestionErrorMessage)
            scale10Container = findViewById(R.id.npsQuestion10Container)
            scale5Container = findViewById(R.id.npsQuestion5Container)
            number0_10 = findViewById(R.id.npsQuestion10_0)
            number1_10 = findViewById(R.id.npsQuestion10_1)
            number2_10 = findViewById(R.id.npsQuestion10_2)
            number3_10 = findViewById(R.id.npsQuestion10_3)
            number4_10 = findViewById(R.id.npsQuestion10_4)
            number5_10 = findViewById(R.id.npsQuestion10_5)
            number6_10 = findViewById(R.id.npsQuestion10_6)
            number7_10 = findViewById(R.id.npsQuestion10_7)
            number8_10 = findViewById(R.id.npsQuestion10_8)
            number9_10 = findViewById(R.id.npsQuestion10_9)
            number10_10 = findViewById(R.id.npsQuestion10_10)

            number1_5 = findViewById(R.id.npsQuestion5_1)
            number2_5 = findViewById(R.id.npsQuestion5_2)
            number3_5 = findViewById(R.id.npsQuestion5_3)
            number4_5 = findViewById(R.id.npsQuestion5_4)
            number5_5 = findViewById(R.id.npsQuestion5_5)
        }
    }

    override fun viewQuestionData() {
        question.let {
            questionTitle?.text = it.Title
            questionTitle?.setText(
                getSpannableTitle(it.Title, it.IsRequired),
                TextView.BufferType.SPANNABLE
            )

            if (it.Scale == 10) {
                scale10Container?.visibility = View.VISIBLE
                scale5Container?.visibility = View.GONE
            } else {
                scale10Container?.visibility = View.GONE
                scale5Container?.visibility = View.VISIBLE
            }
        }
    }

    override fun handleUiEvents() {
        number0_10?.setOnClickListener {
            onNumberClicked(0, number0_10)
        }
        number1_10?.setOnClickListener {
            onNumberClicked(1, number1_10)
        }
        number2_10?.setOnClickListener {
            onNumberClicked(2, number2_10)
        }
        number3_10?.setOnClickListener {
            onNumberClicked(3, number3_10)
        }
        number4_10?.setOnClickListener {
            onNumberClicked(4, number4_10)
        }
        number5_10?.setOnClickListener {
            onNumberClicked(5, number5_10)
        }
        number6_10?.setOnClickListener {
            onNumberClicked(6, number6_10)
        }
        number7_10?.setOnClickListener {
            onNumberClicked(7, number7_10)
        }
        number8_10?.setOnClickListener {
            onNumberClicked(8, number8_10)
        }
        number9_10?.setOnClickListener {
            onNumberClicked(9, number9_10)
        }
        number10_10?.setOnClickListener {
            onNumberClicked(10, number10_10)
        }
        number1_5?.setOnClickListener {
            onNumberClicked(1, number1_5)
        }
        number2_5?.setOnClickListener {
            onNumberClicked(2, number2_5)
        }
        number3_5?.setOnClickListener {
            onNumberClicked(3, number3_5)
        }
        number4_5?.setOnClickListener {
            onNumberClicked(4, number4_5)
        }
        number5_5?.setOnClickListener {
            onNumberClicked(5, number5_5)
        }
    }

    override fun showError() {
        isAnswerValid = false
//        errorMessage?.visibility = View.VISIBLE
    }

    override fun getAnswer(): Answer? {
        question.apply {
            val answer = Answer(
                QuestionGUID, QuestionID, null, selectedNumber, null
            )
            return getValidAnswer(answer)
        }
    }

    private fun onNumberClicked(value: Int, selectedView: TextView?) {
        if (selectedView != lastSelectedNumber) {
            // select option
            isAnswerValid = true
            errorMessage?.visibility = View.GONE
            selectedNumber = value
            maximizeSelectedNumber(selectedView)
            lastSelectedNumber?.let {
                minimizeNumber(it)
            }
            lastSelectedNumber = selectedView

            question.apply {
                answerHandler?.onAnswerClicked(
                    Answer(
                        QuestionGUID, QuestionID, null, selectedNumber, null
                    ), question
                )
            }
        }
    }

    private fun maximizeSelectedNumber(selectedView: TextView?) {
        val params = selectedView?.layoutParams
        context?.resources?.apply {
            params?.height = getDimension(com.intuit.sdp.R.dimen._38sdp).toInt()
            params?.width = getDimension(com.intuit.sdp.R.dimen._38sdp).toInt()
        }
        selectedView?.layoutParams = params
    }

    private fun minimizeNumber(unSelectedView: TextView) {
        val params = unSelectedView.layoutParams
        context?.resources?.apply {
            params.height = getDimension(com.intuit.sdp.R.dimen._32sdp).toInt()
            params.width = getDimension(com.intuit.sdp.R.dimen._32sdp).toInt()
        }
        unSelectedView.layoutParams = params
    }
}
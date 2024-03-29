package com.isthive.ist.questionnaire.questionsViews.rating

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.StarShape
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView

internal class RatingQuestion internal constructor(
    context: Context,
    question: Question,
    resourceLayout: Int = R.layout.rating_question
) :
    BaseQuestionView(context, question, resourceLayout) {

    private var normalRatingIcon: Int? = null
    private var selectedRatingIcon: Int? = null
    private var normalColor: Int? = null
    private var selectedColor: Int? = null

    private var questionTitle: TextView? = null
    private var errorMessage: TextView? = null
    private var selectedRate: Int? = null

    private var rating1Text: TextView? = null
    private var rating2Text: TextView? = null
    private var rating3Text: TextView? = null
    private var rating4Text: TextView? = null
    private var rating5Text: TextView? = null

    private var rating1Icon: AppCompatImageView? = null
    private var rating2Icon: AppCompatImageView? = null
    private var rating3Icon: AppCompatImageView? = null
    private var rating4Icon: AppCompatImageView? = null
    private var rating5Icon: AppCompatImageView? = null

    private var rating1Line: View? = null
    private var rating2Line: View? = null
    private var rating3Line: View? = null
    private var rating4Line: View? = null
    private var rating5Line: View? = null

    private var rating1Container: LinearLayout? = null
    private var rating2Container: LinearLayout? = null
    private var rating3Container: LinearLayout? = null
    private var rating4Container: LinearLayout? = null
    private var rating5Container: LinearLayout? = null

    override fun initViews(view: View?) {
        view?.apply {
            questionTitle = findViewById(R.id.ratingQuestionTitle)
            questionDescription = findViewById(R.id.ratingQuestionDescription)
            errorMessage = findViewById(R.id.ratingQuestionErrorMessage)
            rating1Text = findViewById(R.id.rating1Text)
            rating2Text = findViewById(R.id.rating2Text)
            rating3Text = findViewById(R.id.rating3Text)
            rating4Text = findViewById(R.id.rating4Text)
            rating5Text = findViewById(R.id.rating5Text)

            rating1Icon = findViewById(R.id.rating1Icon)
            rating2Icon = findViewById(R.id.rating2Icon)
            rating3Icon = findViewById(R.id.rating3Icon)
            rating4Icon = findViewById(R.id.rating4Icon)
            rating5Icon = findViewById(R.id.rating5Icon)

            rating1Line = findViewById(R.id.rating1Line)
            rating2Line = findViewById(R.id.rating2Line)
            rating3Line = findViewById(R.id.rating3Line)
            rating4Line = findViewById(R.id.rating4Line)
            rating5Line = findViewById(R.id.rating5Line)

            rating1Container = findViewById(R.id.rating1Container)
            rating2Container = findViewById(R.id.rating2Container)
            rating3Container = findViewById(R.id.rating3Container)
            rating4Container = findViewById(R.id.rating4Container)
            rating5Container = findViewById(R.id.rating5Container)
        }
    }

    override fun viewQuestionData() {
        question.let {
            questionTitle?.text = it.Title
            questionTitle?.setText(
                getSpannableTitle(it.Title, it.IsRequired),
                TextView.BufferType.SPANNABLE
            )

            when (it.StarOption?.Shape) {
                StarShape.STAR -> {
                    normalRatingIcon = R.drawable.ic_star
                    selectedRatingIcon = R.drawable.ic_star_filled
                }
                StarShape.SMILE -> {
                    normalRatingIcon = R.drawable.ic_smiley
                    selectedRatingIcon = R.drawable.ic_smiley
                }
                StarShape.HEART -> {
                    normalRatingIcon = R.drawable.ic_heart
                    selectedRatingIcon = R.drawable.ic_heart_filled
                }
                StarShape.THUMB -> {
                    normalRatingIcon = R.drawable.ic_thumb
                    selectedRatingIcon = R.drawable.ic_thumb_filled
                }
                null -> {
                    normalRatingIcon = null
                    selectedRatingIcon = null
                }
            }
            it.StarOption?.NormalColor?.let { normal ->
                if (normal.isNotBlank()) {
                    normalColor = Color.parseColor("#$normal")
                }
            }
            it.StarOption?.FillColor?.let { fill ->
                if (fill.isNotBlank()) {
                    selectedColor = Color.parseColor("#$fill")
                    setLinesColor(selectedColor!!)
                }
            }
            viewInitialIcons()
        }
    }

    override fun handleUiEvents() {
        rating1Container?.setOnClickListener {
            hideError()
            on1RatingClicked()
        }
        rating2Container?.setOnClickListener {
            hideError()
            on2RatingClicked()
        }
        rating3Container?.setOnClickListener {
            hideError()
            on3RatingClicked()
        }
        rating4Container?.setOnClickListener {
            hideError()
            on4RatingClicked()
        }
        rating5Container?.setOnClickListener {
            hideError()
            on5RatingClicked()
        }
    }

    override fun showError() {
        isAnswerValid = false
//        errorMessage?.visibility = View.VISIBLE
    }

    private fun hideError() {
        isAnswerValid = true
//        errorMessage?.visibility = View.GONE
    }

    override fun getAnswer(): Answer? {
        question.apply {
            val answer = Answer(
                QuestionGUID, QuestionID, null, selectedRate, null
            )
            return getValidAnswer(answer)
        }
    }

    private fun viewInitialIcons() {
        showNotSelectedIcon(rating1Icon)
        showNotSelectedIcon(rating2Icon)
        showNotSelectedIcon(rating3Icon)
        showNotSelectedIcon(rating4Icon)
        showNotSelectedIcon(rating5Icon)
        showNotSelectedData(rating1Text, rating1Line)
        showNotSelectedData(rating2Text, rating2Line)
        showNotSelectedData(rating3Text, rating3Line)
        showNotSelectedData(rating4Text, rating4Line)
        showNotSelectedData(rating5Text, rating5Line)
    }

    private fun on1RatingClicked() {
        selectedRate = 1
        showSelectedData(rating1Text, rating1Line)
        showSelectedIcon(rating1Icon)

        showNotSelectedData(rating2Text, rating2Line)
        showNotSelectedIcon(rating2Icon)
        showNotSelectedData(rating3Text, rating3Line)
        showNotSelectedIcon(rating3Icon)
        showNotSelectedData(rating4Text, rating4Line)
        showNotSelectedIcon(rating4Icon)
        showNotSelectedData(rating5Text, rating5Line)
        showNotSelectedIcon(rating5Icon)

        onRatingSelected()
    }

    private fun on2RatingClicked() {
        selectedRate = 2
        showSelectedIcon(rating1Icon)
        showSelectedIcon(rating2Icon)
        showSelectedData(rating2Text, rating2Line)

        showNotSelectedData(rating1Text, rating1Line)
        showNotSelectedData(rating3Text, rating3Line)
        showNotSelectedIcon(rating3Icon)
        showNotSelectedData(rating4Text, rating4Line)
        showNotSelectedIcon(rating4Icon)
        showNotSelectedData(rating5Text, rating5Line)
        showNotSelectedIcon(rating5Icon)

        onRatingSelected()
    }

    private fun on3RatingClicked() {
        selectedRate = 3
        showSelectedIcon(rating1Icon)
        showSelectedIcon(rating2Icon)
        showSelectedIcon(rating3Icon)
        showSelectedData(rating3Text, rating3Line)

        showNotSelectedData(rating1Text, rating1Line)
        showNotSelectedData(rating2Text, rating2Line)
        showNotSelectedData(rating4Text, rating4Line)
        showNotSelectedIcon(rating4Icon)
        showNotSelectedData(rating5Text, rating5Line)
        showNotSelectedIcon(rating5Icon)

        onRatingSelected()
    }

    private fun on4RatingClicked() {
        selectedRate = 4
        showSelectedIcon(rating1Icon)
        showSelectedIcon(rating2Icon)
        showSelectedIcon(rating3Icon)
        showSelectedIcon(rating4Icon)
        showSelectedData(rating4Text, rating4Line)

        showNotSelectedData(rating1Text, rating1Line)
        showNotSelectedData(rating2Text, rating2Line)
        showNotSelectedData(rating3Text, rating3Line)
        showNotSelectedData(rating5Text, rating5Line)
        showNotSelectedIcon(rating5Icon)

        onRatingSelected()
    }

    private fun on5RatingClicked() {
        selectedRate = 5
        showSelectedIcon(rating1Icon)
        showSelectedIcon(rating2Icon)
        showSelectedIcon(rating3Icon)
        showSelectedIcon(rating4Icon)
        showSelectedIcon(rating5Icon)
        showSelectedData(rating5Text, rating5Line)

        showNotSelectedData(rating1Text, rating1Line)
        showNotSelectedData(rating2Text, rating2Line)
        showNotSelectedData(rating3Text, rating3Line)
        showNotSelectedData(rating4Text, rating4Line)

        onRatingSelected()
    }

    private fun onRatingSelected() {
        question.apply {
            answerHandler?.onAnswerClicked(
                Answer(
                    QuestionGUID, QuestionID, null, selectedRate, null
                ), question
            )
        }
    }

    private fun setLinesColor(fillColor: Int) {
        rating1Line?.setBackgroundColor(fillColor)
        rating2Line?.setBackgroundColor(fillColor)
        rating3Line?.setBackgroundColor(fillColor)
        rating4Line?.setBackgroundColor(fillColor)
        rating5Line?.setBackgroundColor(fillColor)
    }

    private fun showSelectedIcon(ratingIcon: AppCompatImageView?) {
        selectedRatingIcon?.let {
            ratingIcon?.setImageResource(it)
            if (selectedColor != null)
                ratingIcon?.setColorFilter(
                    selectedColor!!,
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
        }
    }

    private fun showNotSelectedIcon(ratingIcon: AppCompatImageView?) {
        normalRatingIcon?.let {
            ratingIcon?.setImageResource(it)
            if (normalColor != null)
                ratingIcon?.setColorFilter(normalColor!!, android.graphics.PorterDuff.Mode.SRC_IN)
        }
    }

    private fun showNotSelectedData(ratingNumber: TextView?, ratingLine: View?) {
        if (normalColor != null) {
            ratingNumber?.setTextColor(normalColor!!)
        } else {
            ratingNumber?.setTextColor(ContextCompat.getColor(context, R.color.gray))
        }
        ratingLine?.visibility = View.GONE
    }

    private fun showSelectedData(ratingNumber: TextView?, ratingLine: View?) {
        if (selectedColor != null) {
            ratingNumber?.setTextColor(selectedColor!!)
        } else {
            ratingNumber?.setTextColor(ContextCompat.getColor(context, R.color.gray))
        }
        ratingLine?.visibility = View.VISIBLE
    }
}
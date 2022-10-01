package com.isthive.ist.questionnaire.questionsViews.rating

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnAttach
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.StarShape
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView

internal class RatingQuestion internal constructor(
    context: Context,
    question: Question?,
    resourceLayout: Int = R.layout.rating_question
) :
    BaseQuestionView(context, question, resourceLayout) {

    private var normalRatingIcon: Int? = null
    private var selectedRatingIcon: Int? = null
    private var normalColor: Int? = null
    private var selectedColor: Int? = null

    private lateinit var questionTitle: TextView
    private lateinit var questionRequired: TextView
    private lateinit var errorMessage: TextView
    private var selectedRate: Int = 0

    private lateinit var rating1Text: TextView
    private lateinit var rating2Text: TextView
    private lateinit var rating3Text: TextView
    private lateinit var rating4Text: TextView
    private lateinit var rating5Text: TextView

    private lateinit var rating1Icon: AppCompatImageView
    private lateinit var rating2Icon: AppCompatImageView
    private lateinit var rating3Icon: AppCompatImageView
    private lateinit var rating4Icon: AppCompatImageView
    private lateinit var rating5Icon: AppCompatImageView

    private lateinit var rating1Line: View
    private lateinit var rating2Line: View
    private lateinit var rating3Line: View
    private lateinit var rating4Line: View
    private lateinit var rating5Line: View

    private lateinit var rating1Container: LinearLayout
    private lateinit var rating2Container: LinearLayout
    private lateinit var rating3Container: LinearLayout
    private lateinit var rating4Container: LinearLayout
    private lateinit var rating5Container: LinearLayout

    override fun initViews(view: View?) {
        view?.apply {
            questionTitle = findViewById(R.id.ratingQuestionTitle)
            questionDescription = findViewById(R.id.ratingQuestionDescription)
            errorMessage = findViewById(R.id.ratingQuestionErrorMessage)
            questionRequired = findViewById(R.id.ratingQuestionRequired)
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
        question?.let {
            questionTitle.text = it.Title
            if(it.IsRequired)
                questionRequired.visibility = View.VISIBLE
            else
                questionRequired.visibility = View.GONE
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
        rating1Container.setOnClickListener {
            hideError()
            on1RatingClicked()
        }
        rating2Container.setOnClickListener {
            hideError()
            on2RatingClicked()
        }
        rating3Container.setOnClickListener {
            hideError()
            on3RatingClicked()
        }
        rating4Container.setOnClickListener {
            hideError()
            on4RatingClicked()
        }
        rating5Container.setOnClickListener {
            hideError()
            on5RatingClicked()
        }
    }

    override fun showError() {
        isAnswerValid = false
        errorMessage.visibility = View.VISIBLE
    }

    private fun hideError(){
        isAnswerValid = true
        errorMessage.visibility = View.GONE
    }

    override fun getAnswer(): Answer? {
        question?.apply {
            return Answer(
                QuestionGUID, QuestionID,null, selectedRate, null
            )
        }
        return null
    }

    private fun viewInitialIcons() {
        showNotSelectedIcon(rating1Icon)
        showNotSelectedIcon(rating2Icon)
        showNotSelectedIcon(rating3Icon)
        showNotSelectedIcon(rating4Icon)
        showNotSelectedIcon(rating5Icon)
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
    }

    private fun setLinesColor(fillColor: Int) {
        rating1Line.setBackgroundColor(fillColor)
        rating2Line.setBackgroundColor(fillColor)
        rating3Line.setBackgroundColor(fillColor)
        rating4Line.setBackgroundColor(fillColor)
        rating5Line.setBackgroundColor(fillColor)
    }

    private fun showSelectedIcon(ratingIcon: AppCompatImageView) {
        selectedRatingIcon?.let {
            ratingIcon.setImageResource(it)
            if (selectedColor != null)
                ratingIcon.setColorFilter(
                    selectedColor!!,
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
        }
    }

    private fun showNotSelectedIcon(ratingIcon: AppCompatImageView) {
        normalRatingIcon?.let {
            ratingIcon.setImageResource(it)
            if (normalColor != null)
                ratingIcon.setColorFilter(normalColor!!, android.graphics.PorterDuff.Mode.SRC_IN)
        }
    }

    private fun showNotSelectedData(ratingNumber: TextView, ratingLine: View) {
        if (normalColor != null) {
            ratingNumber.setTextColor(normalColor!!)
        } else {
            ratingNumber.setTextColor(ContextCompat.getColor(context, R.color.gray))
        }
        ratingLine.visibility = View.GONE
    }

    private fun showSelectedData(ratingNumber: TextView, ratingLine: View) {
        if (selectedColor != null) {
            ratingNumber.setTextColor(selectedColor!!)
        } else {
            ratingNumber.setTextColor(ContextCompat.getColor(context, R.color.gray))
        }
        ratingLine.visibility = View.VISIBLE
    }
}
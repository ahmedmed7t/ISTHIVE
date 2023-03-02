package com.isthive.ist.questionnaire.questionsViews.emoji

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Answer
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.Question
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView

internal class EmojiQuestion internal constructor(
    context: Context,
    question: Question,
    resourceFile: Int = R.layout.emoji_question
) :
    BaseQuestionView(context, question, resourceFile) {

    private var is5EmojiMode = true
    private var lastEmojiClicked: AppCompatImageView? = null

    private var questionTitle: TextView? = null
    private var angryEmoji: AppCompatImageView? = null
    private var frownEmoji: AppCompatImageView? = null
    private var neutralEmoji: AppCompatImageView? = null
    private var smileEmoji: AppCompatImageView? = null
    private var happyEmoji: AppCompatImageView? = null

    private var angryEmojiLine: AppCompatImageView? = null
    private var frownEmojiLine: AppCompatImageView? = null
    private var neutralEmojiLine: AppCompatImageView? = null
    private var smileEmojiLine: AppCompatImageView? = null
    private var happyEmojiLine: AppCompatImageView? = null

    private var lastSelectedLine: AppCompatImageView? = null

    private var firstSpace: View? = null
    private var secondSpace: View? = null
    private var thirdSpace: View? = null
    private var fourthSpace: View? = null
    private var frontSpace: View? = null
    private var lastSpace: View? = null

    private var errorMessage: TextView? = null

    private var selectedEmoji = 0

    override fun initViews(view: View?) {
        view?.apply {
            questionTitle = findViewById(R.id.emojiQuestionTitle)
            questionDescription = findViewById(R.id.emojiQuestionDescription)

            angryEmoji = findViewById(R.id.emojiQuestionAngryEmoji)
            frownEmoji = findViewById(R.id.emojiQuestionFrownEmoji)
            neutralEmoji = findViewById(R.id.emojiQuestionNeutralEmoji)
            smileEmoji = findViewById(R.id.emojiQuestionSmileEmoji)
            happyEmoji = findViewById(R.id.emojiQuestionHappyEmoji)

            angryEmojiLine = findViewById(R.id.emojiQuestionAngryEmojiLine)
            frownEmojiLine = findViewById(R.id.emojiQuestionFrownEmojiLine)
            neutralEmojiLine = findViewById(R.id.emojiQuestionNeutralEmojiLine)
            smileEmojiLine = findViewById(R.id.emojiQuestionSmileEmojiLine)
            happyEmojiLine = findViewById(R.id.emojiQuestionHappyEmojiLine)

            firstSpace = findViewById(R.id.emojiQuestionFirstSpace)
            secondSpace = findViewById(R.id.emojiQuestionSecondSpace)
            thirdSpace = findViewById(R.id.emojiQuestionThirdSpace)
            fourthSpace = findViewById(R.id.emojiQuestionFourthSpace)
            frontSpace = findViewById(R.id.emojiQuestionFrontSpace)
            lastSpace = findViewById(R.id.emojiQuestionLastSpace)

            errorMessage = findViewById(R.id.emojiQuestionErrorMessage)
        }
    }

    override fun viewQuestionData() {
        question.apply {
            questionTitle?.text = Title
            if (IsRequired)
                questionTitle?.setText(getSpannableTitle(Title), TextView.BufferType.SPANNABLE)
            else
                questionTitle?.text = Title
            when (TemplateID) {
                MODERN_STYLE -> enableModernMode()
                CLASSIC_LIGHT -> enableClassicLightMode()
                CLASSIC -> enableClassicMode()
                CLASSIC_II -> enableClassicIIMode()
            }

            when (Scale) {
                2 -> enable2EmojiMode()
                3 -> enable3EmojiMode()
                5 -> enable5EmojiMode()
            }
        }
    }

    override fun handleUiEvents() {
        angryEmoji?.setOnClickListener {
            selectedEmoji = 1
            onEmojiClicked(angryEmoji)
        }
        frownEmoji?.setOnClickListener {
            selectedEmoji = 2
            onEmojiClicked(frownEmoji)
        }
        neutralEmoji?.setOnClickListener {
            question.let {
                when (it.Scale) {
                    3 -> {
                        selectedEmoji = 2
                    }
                    5 -> {
                        selectedEmoji = 3
                    }
                }
            }
            onEmojiClicked(neutralEmoji)
        }
        smileEmoji?.setOnClickListener {
            selectedEmoji = 4
            onEmojiClicked(smileEmoji)
        }
        happyEmoji?.setOnClickListener {
            question.let {
                selectedEmoji = it.Scale
            }
            onEmojiClicked(happyEmoji)
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

    override fun getAnswer(): Answer {
        val answer = Answer(question.QuestionGUID, question.QuestionID, null)

        if(isAnswerValid)
            answer.NumberResponse = selectedEmoji

        return answer
    }

    private fun onEmojiClicked(emoji: AppCompatImageView?) {
        hideError()
        showEmojiLine(emoji)
        context?.resources?.apply {
            var normalSize = getDimension(com.intuit.sdp.R.dimen._40sdp).toInt()
            var clickedSize = getDimension(com.intuit.sdp.R.dimen._45sdp).toInt()
            if (!is5EmojiMode) {
                normalSize = getDimension(com.intuit.sdp.R.dimen._50sdp).toInt()
                clickedSize = getDimension(com.intuit.sdp.R.dimen._55sdp).toInt()
            }

            setEmojiSize(emoji, clickedSize)
            lastEmojiClicked?.let { lastClicked ->
                setEmojiSize(lastClicked, normalSize)
            }

            lastEmojiClicked = emoji
        }

        question.apply {
            answerHandler?.onAnswerClicked(
                Answer(QuestionGUID, QuestionID, null, selectedEmoji), question
            )
        }
    }

    private fun showEmojiLine(emoji: AppCompatImageView?){
        lastSelectedLine?.visibility = View.INVISIBLE
        when(emoji){
            angryEmoji -> {
                angryEmojiLine?.visibility = View.VISIBLE
                lastSelectedLine = angryEmojiLine
            }
            frownEmoji -> {
                frownEmojiLine?.visibility = View.VISIBLE
                lastSelectedLine = frownEmojiLine
            }
            neutralEmoji -> {
                neutralEmojiLine?.visibility = View.VISIBLE
                lastSelectedLine = neutralEmojiLine
            }
            smileEmoji -> {
                smileEmojiLine?.visibility = View.VISIBLE
                lastSelectedLine = smileEmojiLine
            }
            happyEmoji -> {
                happyEmojiLine?.visibility = View.VISIBLE
                lastSelectedLine = happyEmojiLine
            }
        }
    }

    private fun enableModernMode() {
        angryEmoji?.setImageResource(R.drawable.ic_modern_angry)
        frownEmoji?.setImageResource(R.drawable.ic_modern_frown)
        neutralEmoji?.setImageResource(R.drawable.ic_modern_neutral)
        smileEmoji?.setImageResource(R.drawable.ic_modern_smile)
        happyEmoji?.setImageResource(R.drawable.ic_modern_happy)
    }

    private fun enableClassicLightMode() {
        angryEmoji?.setImageResource(R.drawable.ic_classic_light_angry)
        frownEmoji?.setImageResource(R.drawable.ic_classic_light_frown)
        neutralEmoji?.setImageResource(R.drawable.ic_classic_light_neutral)
        smileEmoji?.setImageResource(R.drawable.ic_classic_light_smile)
        happyEmoji?.setImageResource(R.drawable.ic_classic_light_happy)
    }

    private fun enableClassicMode() {
        angryEmoji?.setImageResource(R.drawable.ic_classic_angry)
        frownEmoji?.setImageResource(R.drawable.ic_classic_frown)
        neutralEmoji?.setImageResource(R.drawable.ic_classic_neutral)
        smileEmoji?.setImageResource(R.drawable.ic_classic_smile)
        happyEmoji?.setImageResource(R.drawable.ic_classic_happy)
    }

    private fun enableClassicIIMode() {
        angryEmoji?.setImageResource(R.drawable.ic_classic_v2_angry)
        frownEmoji?.setImageResource(R.drawable.ic_classic_v2_frown)
        neutralEmoji?.setImageResource(R.drawable.ic_classic_v2_neutral)
        smileEmoji?.setImageResource(R.drawable.ic_classic_v2_smile)
        happyEmoji?.setImageResource(R.drawable.ic_classic_v2_happy)
    }

    private fun enable2EmojiMode() {
        is5EmojiMode = false
        angryEmoji?.visibility = View.VISIBLE
        frownEmoji?.visibility = View.GONE
        neutralEmoji?.visibility = View.GONE
        smileEmoji?.visibility = View.GONE
        happyEmoji?.visibility = View.VISIBLE

        firstSpace?.visibility = View.VISIBLE
        secondSpace?.visibility = View.GONE
        thirdSpace?.visibility = View.GONE
        fourthSpace?.visibility = View.GONE
        frontSpace?.visibility = View.VISIBLE
        lastSpace?.visibility = View.VISIBLE

        context?.apply {
            val size = resources.getDimension(com.intuit.sdp.R.dimen._50sdp).toInt()
            setEmojiSize(angryEmoji, size)
            setEmojiSize(happyEmoji, size)
        }
    }

    private fun enable3EmojiMode() {
        is5EmojiMode = false
        angryEmoji?.visibility = View.VISIBLE
        frownEmoji?.visibility = View.GONE
        neutralEmoji?.visibility = View.VISIBLE
        smileEmoji?.visibility = View.GONE
        happyEmoji?.visibility = View.VISIBLE

        firstSpace?.visibility = View.VISIBLE
        secondSpace?.visibility = View.GONE
        thirdSpace?.visibility = View.GONE
        fourthSpace?.visibility = View.VISIBLE

        frontSpace?.visibility = View.VISIBLE
        lastSpace?.visibility = View.VISIBLE
        context?.apply {
            val size = resources.getDimension(com.intuit.sdp.R.dimen._50sdp).toInt()
            setEmojiSize(angryEmoji, size)
            setEmojiSize(neutralEmoji, size)
            setEmojiSize(happyEmoji, size)
        }
    }

    private fun enable5EmojiMode() {
        is5EmojiMode = true
        angryEmoji?.visibility = View.VISIBLE
        frownEmoji?.visibility = View.VISIBLE
        neutralEmoji?.visibility = View.VISIBLE
        smileEmoji?.visibility = View.VISIBLE
        happyEmoji?.visibility = View.VISIBLE

        firstSpace?.visibility = View.VISIBLE
        secondSpace?.visibility = View.VISIBLE
        thirdSpace?.visibility = View.VISIBLE
        fourthSpace?.visibility = View.VISIBLE
        frontSpace?.visibility = View.GONE
        lastSpace?.visibility = View.GONE
        context?.apply {
            val size = resources.getDimension(com.intuit.sdp.R.dimen._40sdp).toInt()
            setEmojiSize(angryEmoji, size)
            setEmojiSize(frownEmoji, size)
            setEmojiSize(neutralEmoji, size)
            setEmojiSize(smileEmoji, size)
            setEmojiSize(happyEmoji, size)
        }
    }

    private fun setEmojiSize(emoji: AppCompatImageView?, size: Int) {
        var params = emoji?.layoutParams
        params?.width = size
        params?.height = size
        emoji?.layoutParams = params
    }

    internal companion object {
        // TemplateId
        const val MODERN_STYLE = "Modern"
        const val CLASSIC_LIGHT = "Line"
        const val CLASSIC = "Normal"
        const val CLASSIC_II = "Bold"
    }
}
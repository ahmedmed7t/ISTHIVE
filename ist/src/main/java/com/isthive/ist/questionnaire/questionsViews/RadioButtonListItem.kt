package com.isthive.ist.questionnaire.questionsViews

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.isthive.ist.R

internal class RadioButtonListItem(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private var view: View? = null

    private lateinit var optionContainer: ConstraintLayout
    private lateinit var optionRadioButton: RadioButton
    private lateinit var optionTitle: TextView
    private lateinit var optionMark: AppCompatImageView
    private val MODERN_STYLE = "Modern"
    private var style: String = "Modern"

    init {
        view = LayoutInflater.from(context).inflate(R.layout.radio_button_list_item, this, true)
        init()
    }

    private fun init() {
        view?.apply {
            optionContainer = findViewById(R.id.radioListItemContainer)
            optionRadioButton = findViewById(R.id.radioListItemRadio)
            optionTitle = findViewById(R.id.radioListItemTitle)
            optionMark = findViewById(R.id.radioListItemRightMark)
        }
    }

    fun setMode(mode: String) {
        style = mode
        if (mode == MODERN_STYLE) {
            viewModernStyle()
        } else {
            viewClassicStyle()
        }
    }

    fun setOptionSelected(isSelected: Boolean) {
        if (isSelected) {
            selectOption()
        } else {
            unSelectOption()
        }
    }

    fun setOptionTitle(title: String) {
        optionTitle.text = title
    }

    private fun viewModernStyle() {
        optionContainer.background = ContextCompat.getDrawable(context, R.drawable.rounded_solid)
        optionRadioButton.visibility = View.GONE
    }

    private fun viewClassicStyle() {
        optionRadioButton.visibility = View.VISIBLE
        optionMark.visibility = View.GONE
        optionContainer.background = null
    }

    private fun selectOption() {
        Log.v("Medhat","selectOption called")
        if (style == MODERN_STYLE) {
            optionContainer.backgroundTintList = ContextCompat.getColorStateList(context, R.color.blue)
            optionTitle.setTextColor(ContextCompat.getColor(context, R.color.white))
            optionMark.visibility = View.VISIBLE
        } else {
            optionRadioButton.isChecked = true
        }
    }

    private fun unSelectOption() {
        if (style == MODERN_STYLE) {
            optionContainer.backgroundTintList = ContextCompat.getColorStateList(context, R.color.light_blue)
            optionTitle.setTextColor(ContextCompat.getColor(context, R.color.blue))
            optionMark.visibility = View.GONE
        } else {
            optionRadioButton.isChecked = false
        }
    }
}
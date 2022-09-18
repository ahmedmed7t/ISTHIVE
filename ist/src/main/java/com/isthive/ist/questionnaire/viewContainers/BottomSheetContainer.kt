package com.isthive.ist.questionnaire.viewContainers

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.NavigationMode
import com.isthive.ist.questionnaire.questionnaireModule.presentation.handlers.QuestionHandler

internal class BottomSheetContainer : BottomSheetDialogFragment() {

    private var popUpMainView: View? = null
    private lateinit var bottomSheetContainer: ConstraintLayout
    private var questionHandler: QuestionHandler? = null

    private var isLastItem = false
    private var navigationMode = NavigationMode.Modern

    private lateinit var topBackButton: AppCompatImageView
    private lateinit var bottomBackButton: AppCompatImageView
    private lateinit var largeNextButton: TextView
    private lateinit var smallNextButton: AppCompatImageView
    private lateinit var smallSubmitButton: TextView
    private lateinit var smallActionsContainer: LinearLayout

    private lateinit var closeButton: AppCompatImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = LayoutInflater.from(context)
            .inflate(R.layout.bottom_sheet_container_view, container, false)
        initViews(view)

        isCancelable = false
        inflateView()
        handleViewEvents()

        return view
    }

    private fun initViews(view: View) {
        view.apply {
            bottomSheetContainer = findViewById(R.id.bottomSheetContainer)
            topBackButton = findViewById(R.id.bottomSheetContainerBack)
            bottomBackButton = findViewById(R.id.bottomSheetSmallBackButton)
            largeNextButton = findViewById(R.id.bottomSheetLargeSubmitButton)
            smallNextButton = findViewById(R.id.bottomSheetNSmallNextButton)
            smallSubmitButton = findViewById(R.id.bottomSheetSmallSubmitButton)
            smallActionsContainer = findViewById(R.id.bottomSheet3ActionsContainer)
            closeButton = findViewById(R.id.bottomSheetContainerClose)
        }
    }

    private fun handleViewEvents() {
        closeButton.setOnClickListener {
            questionHandler?.onCloseClicked()
        }
        topBackButton.setOnClickListener {
            questionHandler?.onBackClicked()
        }
        bottomBackButton.setOnClickListener {
            questionHandler?.onBackClicked()
        }
        smallNextButton.setOnClickListener {
            questionHandler?.onNextClicked()
        }
        smallSubmitButton.setOnClickListener {
            questionHandler?.onSubmitClicked()
        }
        largeNextButton.setOnClickListener {
            if (isLastItem)
                questionHandler?.onSubmitClicked()
            else
                questionHandler?.onNextClicked()
        }
    }

    fun mainView(popUpContent: View): BottomSheetContainer {
        this.popUpMainView = popUpContent
        return this
    }

    fun setHandler(questionHandler: QuestionHandler): BottomSheetContainer {
        this.questionHandler = questionHandler
        return this
    }

    fun setNavigationMode(navigationMode: NavigationMode): BottomSheetContainer {
        this.navigationMode = navigationMode
        return this
    }


    fun addView(popUpContent: View, isLastItem: Boolean, isFirstItem: Boolean) {
        this.isLastItem = isLastItem
        if (navigationMode == NavigationMode.Modern)
            handleModernNavigation(isLastItem, isFirstItem)
        else
            handleClassicNavigation(isLastItem)
        popUpContent.layoutParams = ViewGroup.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        bottomSheetContainer.apply {
            removeView(popUpMainView)
            addView(popUpContent)
            popUpMainView = popUpContent
        }
    }

    private fun handleClassicNavigation(isLastItem: Boolean) {
        if (isLastItem) {
            largeNextButton.text = context?.resources?.getString(R.string.submit)
        } else {
            largeNextButton.text = context?.resources?.getString(R.string.next)
        }
    }

    private fun handleModernNavigation(isLastItem: Boolean, isFirstItem: Boolean) {
        smallSubmitButton.backgroundTintList =
            context?.let { ContextCompat.getColorStateList(it, R.color.white_blue) }
        context?.let {
            smallSubmitButton.setTextColor(
                ContextCompat.getColor(it, R.color.fade_gray)
            )
        }

        if (isFirstItem) {
            bottomBackButton.backgroundTintList =
                context?.let { ContextCompat.getColorStateList(it, R.color.white_blue) }
            bottomBackButton.imageTintList =
                context?.let { ContextCompat.getColorStateList(it, R.color.fade_gray) }
        } else {
            bottomBackButton.backgroundTintList =
                context?.let { ContextCompat.getColorStateList(it, R.color.blue) }
            bottomBackButton.imageTintList =
                context?.let { ContextCompat.getColorStateList(it, R.color.white) }
        }

        if (isLastItem) {
            smallNextButton.backgroundTintList =
                context?.let { ContextCompat.getColorStateList(it, R.color.white_blue) }
            smallNextButton.imageTintList =
                context?.let { ContextCompat.getColorStateList(it, R.color.fade_gray) }

            smallSubmitButton.backgroundTintList =
                context?.let { ContextCompat.getColorStateList(it, R.color.blue) }
            context?.let {
                smallSubmitButton.setTextColor(
                    ContextCompat.getColor(it, R.color.white)
                )
            }

        } else {
            smallNextButton.backgroundTintList =
                context?.let { ContextCompat.getColorStateList(it, R.color.blue) }
            smallNextButton.imageTintList =
                context?.let { ContextCompat.getColorStateList(it, R.color.white) }
        }
    }

    private fun inflateView() {
        popUpMainView?.layoutParams = ViewGroup.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        bottomSheetContainer.apply {
            addView(popUpMainView)
        }
        if (navigationMode == NavigationMode.Modern) {
            enableModernNavigationMode()
            handleModernNavigation(isLastItem = false, isFirstItem = true)
        } else {
            enableClassicNavigationMode()
            handleClassicNavigation(false)
        }
    }

    private fun enableClassicNavigationMode() {
        topBackButton.visibility = View.VISIBLE
        largeNextButton.visibility = View.VISIBLE

        smallActionsContainer.visibility = View.GONE
    }

    private fun enableModernNavigationMode() {
        topBackButton.visibility = View.GONE
        largeNextButton.visibility = View.GONE

        smallActionsContainer.visibility = View.VISIBLE
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
        }
    }
}
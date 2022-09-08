package com.isthive.ist.questionnaire.viewContainers

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.presentation.handlers.QuestionHandler

internal class BottomSheetContainer : BottomSheetDialogFragment() {

    private var popUpMainView: View? = null
    private lateinit var bottomSheetContainer: ConstraintLayout
    private var questionHandler: QuestionHandler? = null

    private lateinit var submitButton: TextView
    private lateinit var nextButton: TextView
    private lateinit var backButton: TextView

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
        bottomSheetContainer = view.findViewById(R.id.bottomSheetContainer)
        submitButton = view.findViewById(R.id.bottomSheetQuestionSubmitButton)
        nextButton = view.findViewById(R.id.bottomSheetQuestionNextButton)
        backButton = view.findViewById(R.id.bottomSheetQuestionBackButton)
    }

    private fun handleViewEvents() {
        submitButton.setOnClickListener {
            questionHandler?.onSubmitClicked()
        }
        nextButton.setOnClickListener {
            questionHandler?.onNextClicked()
        }
        backButton.setOnClickListener {
            questionHandler?.onBackClicked()
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

    fun addView(popUpContent: View) {
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

    private fun inflateView() {
        popUpMainView?.layoutParams = ViewGroup.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        bottomSheetContainer.apply {
            addView(popUpMainView)
        }
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
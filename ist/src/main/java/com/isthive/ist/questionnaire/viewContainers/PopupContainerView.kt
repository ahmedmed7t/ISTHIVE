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
import androidx.fragment.app.DialogFragment
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.presentation.handlers.QuestionHandler

/**
 * This view is just a container that is designed to hold a view inside
 * and show it as a dialog at the middle of the screen.
 *
 * It is designed by builder pattern and doesn't take any params.
 *
 * functions:
 *  - mainView  -> take a view as param and inflate it as main content of dialog
 *
 *  example:
 *
 *  PopupContainerView()
 *      .mainView(your custom view)
 *      .show(supportFragmentManager, "tag")
 */
internal class PopupContainerView : DialogFragment() {

    private var popUpMainView: View? = null
    private lateinit var popUpContainer: ConstraintLayout
    private lateinit var topBackButton: AppCompatImageView
    private lateinit var bottomBackButton: AppCompatImageView
    private lateinit var largeNextButton: TextView
    private lateinit var smallNextButton: TextView
    private lateinit var smallSubmitButton: TextView
    private lateinit var smallActionsContainer: LinearLayout

    private var questionHandler: QuestionHandler? = null
    private var isLastItem = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view =
            LayoutInflater.from(context).inflate(R.layout.popup_conatiner_view, container, false)
        initViews(view)
        handleViewEvents()

        isCancelable = true
        inflateView()

        return view
    }

    private fun initViews(view: View) {
        view.apply {
            popUpContainer = findViewById(R.id.popUpContainer)
            topBackButton = findViewById(R.id.popUpContainerBack)
            bottomBackButton = findViewById(R.id.popUpSmallBackButton)
            largeNextButton = findViewById(R.id.popUpLargeSubmitButton)
            smallNextButton = findViewById(R.id.popUpNSmallNextButton)
            smallSubmitButton = findViewById(R.id.popUpSmallSubmitButton)
            smallActionsContainer = findViewById(R.id.popUp3ActionsContainer)
        }
    }

    fun mainView(popUpContent: View): PopupContainerView {
        this.popUpMainView = popUpContent
        return this
    }

    fun setHandler(questionHandler: QuestionHandler): PopupContainerView {
        this.questionHandler = questionHandler
        return this
    }

    private fun inflateView() {
        popUpMainView?.layoutParams = ViewGroup.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        popUpContainer.apply {
            addView(popUpMainView)
        }
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

    private fun handleViewEvents(){
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
            if(isLastItem)
                questionHandler?.onSubmitClicked()
            else
                questionHandler?.onNextClicked()
        }
    }

    fun enableClassicNavigationMode() {
        topBackButton.visibility = View.VISIBLE
        largeNextButton.visibility = View.VISIBLE

        smallActionsContainer.visibility = View.GONE
    }

    fun enableModernNavigationMode() {
        topBackButton.visibility = View.GONE
        largeNextButton.visibility = View.GONE

        smallActionsContainer.visibility = View.VISIBLE
    }
}
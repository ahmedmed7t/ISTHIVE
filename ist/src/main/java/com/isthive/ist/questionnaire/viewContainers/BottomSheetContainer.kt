package com.isthive.ist.questionnaire.viewContainers

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.NavigationMode
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.WelcomeMessage
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.WelcomeMode
import com.isthive.ist.questionnaire.questionnaireModule.presentation.handlers.QuestionHandler
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView

internal class BottomSheetContainer : BottomSheetDialogFragment(), ContainersContract {

    private var popUpMainView: View? = null
    private lateinit var bottomSheetContainer: ConstraintLayout
    private var questionHandler: QuestionHandler? = null

    private var isLastItem = false
    private var isSingleQuestion = false
    private var navigationMode = NavigationMode.Modern
    private var welcomeMessage: WelcomeMessage? = null
    private var hasCloseButton: Boolean = false
    private var hasProgressBar: Boolean = false

    private lateinit var topBackButton: AppCompatImageView
    private lateinit var bottomBackButton: TextView
    private lateinit var largeNextButton: TextView
    private lateinit var smallNextButton: TextView
    private lateinit var smallSubmitButton: TextView
    private lateinit var smallActionsContainer: LinearLayout
    private lateinit var allActionsContainer: ConstraintLayout

    private lateinit var welcomeContainer: LinearLayout
    private lateinit var welcomeTitle: TextView
    private lateinit var welcomeDescription: TextView
    private lateinit var takeSurveyButton: TextView

    private lateinit var closeButton: AppCompatImageView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = LayoutInflater.from(context)
            .inflate(R.layout.bottom_sheet_container_view, container, false)
        initViews(view)

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
            allActionsContainer = findViewById(R.id.bottomSheetActionContainer)
            closeButton = findViewById(R.id.bottomSheetContainerClose)
            progressBar = findViewById(R.id.bottomSheetContainerProgress)

            welcomeContainer = findViewById(R.id.bottomSheetWelcomeContainer)
            welcomeTitle = findViewById(R.id.bottomSheetWelcomeTitle)
            welcomeDescription = findViewById(R.id.bottomSheetWelcomeMessage)
            takeSurveyButton = findViewById(R.id.bottomSheetWelcomeTakeSurvey)

        }
    }

    private fun handleViewEvents() {
        closeButton.setOnClickListener {
            dismiss()
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

        takeSurveyButton.setOnClickListener {
            hideWelcomeMessage()
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

    fun isSingleQuestion(singleQuestion: Boolean): BottomSheetContainer {
        this.isSingleQuestion = singleQuestion
        return this
    }

    fun setHasCloseButton(hasCloseButton: Boolean): BottomSheetContainer {
        this.hasCloseButton = hasCloseButton
        return this
    }

    fun setHasProgressBar(hasProgressBar: Boolean): BottomSheetContainer {
        this.hasProgressBar = hasProgressBar
        return this
    }

    fun setWelcomeMessage(welcomeMessage: WelcomeMessage?): BottomSheetContainer {
        this.welcomeMessage = welcomeMessage
        return this
    }

    override fun addView(popUpContent: View, isLastItem: Boolean, isFirstItem: Boolean) {
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

    override fun dismissContainer() {
        dismiss()
    }

    override fun updateProgressBar(progress: Int) {
        progressBar.progress = progress
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        questionHandler?.onDismiss()
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
            bottomBackButton.visibility = View.INVISIBLE
//            bottomBackButton.backgroundTintList =
//                context?.let { ContextCompat.getColorStateList(it, R.color.white_blue) }
//            context?.let {
//                bottomBackButton.setTextColor(
//                    ContextCompat.getColorStateList(
//                        it,
//                        R.color.fade_gray
//                    )
//                )
//            }
        } else {
            bottomBackButton.visibility = View.VISIBLE
//            bottomBackButton.backgroundTintList =
//                context?.let { ContextCompat.getColorStateList(it, R.color.blue) }
//            context?.let {
//                bottomBackButton.setTextColor(
//                    ContextCompat.getColorStateList(it, R.color.white)
//                )
//            }
        }

        if (isLastItem) {
            smallNextButton.backgroundTintList =
                context?.let { ContextCompat.getColorStateList(it, R.color.white_blue) }
            context?.let {
                smallNextButton.setTextColor(
                    ContextCompat.getColorStateList(it, R.color.fade_gray)
                )
            }

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
            context?.let {
                smallNextButton.setTextColor(
                    ContextCompat.getColorStateList(it, R.color.white)
                )
            }
        }
    }

    private fun inflateView() {
        popUpMainView?.layoutParams = ViewGroup.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        if (navigationMode == NavigationMode.Modern) {
            enableModernNavigationMode()
            handleModernNavigation(isLastItem = false, isFirstItem = true)
        } else {
            enableClassicNavigationMode()
            handleClassicNavigation(false)
        }

        if (isSingleQuestion)
            handleSingleQuestionMode()

        if (hasCloseButton)
            closeButton.visibility = View.VISIBLE

        if (hasProgressBar)
            progressBar.visibility = View.VISIBLE

        bottomSheetContainer.apply {
            addView(popUpMainView)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            hideWelcomeMessage()
            welcomeMessage?.let {
                if (it.Mode == WelcomeMode.Separate_View) {
                    viewWelcomeMessage(it.Title, it.SubTitle)
                } else if (it.Mode == WelcomeMode.First_Question) {
                    (popUpMainView as BaseQuestionView).showWelcomeMessage(it.SubTitle)
                }
            }
        }, 150)
    }

    private fun handleSingleQuestionMode() {
        if (navigationMode == NavigationMode.Modern) {
            enableSingleQuestionModernNavigation()
        } else {
            enableSingleQuestionClassicNavigation()
        }
    }

    private fun enableSingleQuestionModernNavigation() {
        smallNextButton.backgroundTintList =
            context?.let { ContextCompat.getColorStateList(it, R.color.white_blue) }
        context?.let {
            smallNextButton.setTextColor(
                ContextCompat.getColorStateList(it, R.color.fade_gray)
            )
        }

        bottomBackButton.visibility = View.INVISIBLE
//        bottomBackButton.backgroundTintList =
//            context?.let { ContextCompat.getColorStateList(it, R.color.white_blue) }
//        context?.let {
//            bottomBackButton.setTextColor(
//                ContextCompat.getColorStateList(it, R.color.fade_gray)
//            )
//        }

        smallSubmitButton.backgroundTintList =
            context?.let { ContextCompat.getColorStateList(it, R.color.blue) }
        context?.let {
            smallSubmitButton.setTextColor(
                ContextCompat.getColor(it, R.color.white)
            )
        }
    }

    private fun enableSingleQuestionClassicNavigation() {
        largeNextButton.text = getString(R.string.submit)
        topBackButton.visibility = View.GONE
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

    private fun viewWelcomeMessage(title: String, description: String) {
        welcomeTitle.text = title
        welcomeDescription.text = description
        welcomeContainer.visibility = View.VISIBLE
        allActionsContainer.visibility = View.GONE
        topBackButton.visibility = View.GONE
    }

    private fun hideWelcomeMessage() {
        welcomeContainer.visibility = View.GONE
        allActionsContainer.visibility = View.VISIBLE
        if (navigationMode == NavigationMode.Classic)
            topBackButton.visibility = View.VISIBLE
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
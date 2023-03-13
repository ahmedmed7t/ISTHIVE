package com.isthive.ist.questionnaire.viewContainers

import android.content.DialogInterface
import android.content.res.Resources
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
import androidx.fragment.app.DialogFragment
import com.isthive.ist.R
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.NavigationMode
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.WelcomeMessage
import com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire.WelcomeMode
import com.isthive.ist.questionnaire.questionnaireModule.presentation.handlers.QuestionHandler
import com.isthive.ist.questionnaire.questionsViews.BaseQuestionView


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
internal class PopupContainerView : DialogFragment(), ContainersContract {

    private var popUpMainView: View? = null
    private lateinit var popUpContainer: ConstraintLayout
    private lateinit var topBackButton: AppCompatImageView
    private lateinit var bottomBackButton: TextView
    private lateinit var largeNextButton: TextView
    private lateinit var smallNextButton: TextView
    private lateinit var smallSubmitButton: TextView
    private lateinit var smallActionsContainer: LinearLayout
    private lateinit var allActionsContainer: ConstraintLayout

    private lateinit var closeButton: AppCompatImageView
    private lateinit var progressBar: ProgressBar

    private lateinit var welcomeContainer: LinearLayout
    private lateinit var welcomeTitle: TextView
    private lateinit var welcomeDescription: TextView
    private lateinit var takeSurveyButton: TextView

    private var questionHandler: QuestionHandler? = null
    private var isLastItem = false
    private var isSingleQuestion = false
    private var navigationMode = NavigationMode.Modern
    private var welcomeMessage: WelcomeMessage? = null
    private var hasCloseButton: Boolean = false
    private var hasProgressBar: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view =
            LayoutInflater.from(context).inflate(R.layout.popup_conatiner_view, container, false)
        initViews(view)
        handleViewEvents()

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
            closeButton = findViewById(R.id.popUpContainerClose)
            progressBar = findViewById(R.id.popUpContainerProgress)
            allActionsContainer = findViewById(R.id.popUpActionContainer)

            welcomeContainer = findViewById(R.id.popUpWelcomeContainer)
            welcomeTitle = findViewById(R.id.popUpWelcomeTitle)
            welcomeDescription = findViewById(R.id.popUpWelcomeMessage)
            takeSurveyButton = findViewById(R.id.popUpWelcomeTakeSurvey)
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

    fun setNavigationMode(navigationMode: NavigationMode?): PopupContainerView {
        navigationMode?.let {
            this.navigationMode = navigationMode
        }
        return this
    }

    fun isSingleQuestion(singleQuestion: Boolean): PopupContainerView {
        this.isSingleQuestion = singleQuestion
        return this
    }

    fun setHasCloseButton(hasCloseButton: Boolean): PopupContainerView {
        this.hasCloseButton = hasCloseButton
        return this
    }

    fun setHasProgressBar(hasProgressBar: Boolean): PopupContainerView {
        this.hasProgressBar = hasProgressBar
        return this
    }

    fun setWelcomeMessage(welcomeMessage: WelcomeMessage?): PopupContainerView {
        this.welcomeMessage = welcomeMessage
        return this
    }

    override fun addView(popUpContent: View, isLastItem: Boolean, isFirstItem: Boolean) {
        this.isLastItem = isLastItem
        if (navigationMode == NavigationMode.Modern)
            handleModernNavigation(isLastItem, isFirstItem)
        else
            handleClassicNavigation(isLastItem, isFirstItem)

        popUpContent.layoutParams = ViewGroup.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        popUpContainer.apply {
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

    private fun handleClassicNavigation(isLastItem: Boolean, isFirstItem: Boolean) {
        if (isLastItem) {
            largeNextButton.text = context?.resources?.getString(R.string.submit)
        } else {
            largeNextButton.text = context?.resources?.getString(R.string.next)
        }

        if(isFirstItem)
            topBackButton.visibility = View.GONE
        else
            topBackButton.visibility = View.VISIBLE
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
        } else {
            bottomBackButton.visibility = View.VISIBLE
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
                smallNextButton.setTextColor(ContextCompat.getColorStateList(it, R.color.white))
            }
        }
    }

    private fun inflateView() {
        popUpMainView?.layoutParams = ViewGroup.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        if (navigationMode == NavigationMode.Modern)
            enableModernNavigationMode()
        else
            enableClassicNavigationMode()

        if (isSingleQuestion)
            handleSingleQuestionMode()

        if (hasCloseButton)
            closeButton.visibility = View.VISIBLE

        if (hasProgressBar)
            progressBar.visibility = View.VISIBLE

        popUpContainer.apply {
            addView(popUpMainView)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            hideWelcomeMessage()
            welcomeMessage?.let {
                if (it.Mode == WelcomeMode.Separate_View) {
                    viewWelcomeMessage(it.Title, it.SubTitle)
                } else if (it.Mode == WelcomeMode.First_Question) {
                    (popUpMainView as BaseQuestionView).showWelcomeMessage(it.Title)
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
            smallNextButton.setTextColor(ContextCompat.getColorStateList(it, R.color.fade_gray))
        }

        bottomBackButton.visibility = View.INVISIBLE

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


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        questionHandler?.onDismiss()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            attributes.width = (getScreenWidth() * .97).toInt()
            setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
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

    private fun enableClassicNavigationMode() {
        topBackButton.visibility = View.GONE
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
    }

    private fun getScreenWidth() = Resources.getSystem().displayMetrics.widthPixels
}
package com.isthive.ist.questionnaire.viewContainers

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.isthive.ist.R

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = LayoutInflater.from(context).inflate(R.layout.popup_conatiner_view, container, false)
        popUpContainer = view.findViewById(R.id.popUpContainer)

        isCancelable = true
        inflateView()

        return view
    }

    fun mainView(popUpContent: View): PopupContainerView {
        this.popUpMainView = popUpContent
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
}
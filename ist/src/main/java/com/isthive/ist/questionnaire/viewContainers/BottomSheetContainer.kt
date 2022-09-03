package com.isthive.ist.questionnaire.viewContainers

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.isthive.ist.R

internal class BottomSheetContainer : BottomSheetDialogFragment() {

    private var popUpMainView: View? = null
    private lateinit var bottomSheetContainer: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_container_view, container, false)
        bottomSheetContainer = view.findViewById(R.id.bottomSheetContainer)

        isCancelable = true
        inflateView()

        return view
    }

    fun mainView(popUpContent: View): BottomSheetContainer {
        this.popUpMainView = popUpContent
        return this
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
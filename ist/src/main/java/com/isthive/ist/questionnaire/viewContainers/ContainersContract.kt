package com.isthive.ist.questionnaire.viewContainers

import android.view.View

internal interface ContainersContract {
    fun addView(popUpContent: View, isLastItem: Boolean, isFirstItem: Boolean)
    fun dismissContainer()
    fun updateProgressBar(progress: Int)
}
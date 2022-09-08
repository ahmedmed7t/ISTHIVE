package com.isthive.ist.questionnaire.questionsViews.multipleChoice

internal interface MultipleChoiceAdapterHandler {
    fun onChoiceSelected(position: Int)
    fun onChoiceUnSelected(position: Int)
}
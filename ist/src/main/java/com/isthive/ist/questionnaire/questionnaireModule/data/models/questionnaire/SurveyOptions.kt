package com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire

internal data class SurveyOptions(
    val DisplayMode: DisplayMode,
    val EnableCloseButton: Boolean,
    val EnablePreviousButton: Boolean,
    val HasProgressBar: Boolean,
    val NavigationMode: NavigationMode,
    val ProgressBarPosition: Int,
    val Theme: Theme
)
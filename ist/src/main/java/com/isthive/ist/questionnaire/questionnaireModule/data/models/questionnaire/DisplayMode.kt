package com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire

import com.google.gson.annotations.SerializedName

enum class DisplayMode {
    @SerializedName("4")
    FullScreen,
    @SerializedName("5")
    Popup,
    @SerializedName("6")
    BottomCard
}
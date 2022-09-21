package com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire

import com.google.gson.annotations.SerializedName

enum class WelcomeMode {
    @SerializedName("0")
    No_Welcome,
    @SerializedName("1")
    First_Question,
    @SerializedName("2")
    Separate_View
}
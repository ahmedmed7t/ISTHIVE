package com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire

import com.google.gson.annotations.SerializedName

enum class NavigationMode {
    @SerializedName("1")
    Modern,
    @SerializedName("2")
    Classic,
}
package com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire

import com.google.gson.annotations.SerializedName

internal enum class StarShape {
    @SerializedName("1")
    STAR,

    @SerializedName("2")
    SMILE,

    @SerializedName("3")
    HEART,

    @SerializedName("4")
    THUMB
}
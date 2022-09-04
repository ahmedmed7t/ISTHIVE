package com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire

import com.google.gson.annotations.SerializedName

internal enum class ChoiceType {
    @SerializedName("0")
    Normal_Choice,
    @SerializedName("1")
    Other_Choice,
}
package com.isthive.ist.questionnaire.questionnaireModule.data.models.questionnaire

import com.google.gson.annotations.SerializedName

enum class QuestionType {
    @SerializedName("2")
    Multiple_choice_question,
    @SerializedName("4")
    List_question,
    @SerializedName("5")
    Date_question,
    @SerializedName("6")
    Slide_question,
    @SerializedName("7")
    Star_question,
    @SerializedName("8")
    NPS,
    @SerializedName("9")
    Text_input,
    @SerializedName("10")
    Number_input,
    @SerializedName("11")
    Email_input,
    @SerializedName("12")
    Phone_number_input,
    @SerializedName("13")
    Postal_code_input,
    @SerializedName("14")
    URL_input,
    @SerializedName("15")
    Single_choice,
    @SerializedName("16")
    EmojiHiveCFM_Mobile_API,
    @SerializedName("17")
    Image_MCQ,
    @SerializedName("18")
    Image_Single_Choice,
    @SerializedName("19")
    CSAT,
    @SerializedName("20")
    FCR,
    @SerializedName("21")
    CES,
    @SerializedName("22")
    Numeric_CSAT,
    @SerializedName("23")
    Numeric_CES,
}
package com.isthive.ist.app.models

import com.google.gson.annotations.SerializedName

data class NoDataResponse(
    @SerializedName("Error", alternate = arrayOf("Message"))
    val error: String?,
    @SerializedName("Code")
    val code: Int?,
)

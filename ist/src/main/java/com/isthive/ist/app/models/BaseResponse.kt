package com.isthive.ist.app.models

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("Error", alternate = arrayOf("Message"))
    val error: String?,
    @SerializedName("Code")
    val code: Int?,
    @SerializedName("Status")
    val Status: Boolean?,
    @SerializedName("Data", alternate = arrayOf("Obj"))
    val data: T?
)

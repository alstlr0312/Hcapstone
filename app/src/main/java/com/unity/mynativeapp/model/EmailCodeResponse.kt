package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName

data class EmailCodeResponse(
    @SerializedName("target") val target: String,
    @SerializedName("code") val code: String
)
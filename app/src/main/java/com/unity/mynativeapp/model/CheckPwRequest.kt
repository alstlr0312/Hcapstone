package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName

data class CheckPwRequest(
    @SerializedName("password") val password: String,
)

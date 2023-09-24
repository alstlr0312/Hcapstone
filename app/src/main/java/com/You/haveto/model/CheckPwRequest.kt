package com.You.haveto.model

import com.google.gson.annotations.SerializedName

data class CheckPwRequest(
    @SerializedName("password") val password: String,
)

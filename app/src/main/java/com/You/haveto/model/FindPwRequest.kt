package com.You.haveto.model

import com.google.gson.annotations.SerializedName

data class FindPwRequest (
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
    )
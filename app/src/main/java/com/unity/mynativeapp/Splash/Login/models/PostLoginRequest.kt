package com.unity.mynativeapp.Splash

import com.google.gson.annotations.SerializedName

data class PostLoginRequest(
    @SerializedName("loginId") val loginId: String,
    @SerializedName("password") val password: String,
)

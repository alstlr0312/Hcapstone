package com.unity.mynativeapp.Splash

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("error") val error: String? = null,
    @SerializedName("data") val data: ResultLogin? = null,
)

data class ResultLogin(
    @SerializedName("grantType") val grantType: String,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
)
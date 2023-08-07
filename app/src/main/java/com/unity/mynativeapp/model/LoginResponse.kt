package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName


data class ResultLogin(
    @SerializedName("grantType") val grantType: String,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
)
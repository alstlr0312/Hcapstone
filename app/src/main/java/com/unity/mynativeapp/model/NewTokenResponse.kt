package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName

data class NewTokenResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("error") val error: String? = null,
    @SerializedName("data") val data: LoginData? = null,
)

package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName
import com.unity.mynativeapp.network.MyResponse

data class SignUpResponse(
    @SerializedName("error")
    val error: String? = null,
    @SerializedName("data")
    val data: String? = null
)

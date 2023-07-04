package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName

data class EmailCodeRequest (
    @SerializedName("email")
    val email: String
    )
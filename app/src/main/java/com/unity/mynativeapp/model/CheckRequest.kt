package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName

data class CheckRequest (
    @SerializedName("email")
    val email: String

    )
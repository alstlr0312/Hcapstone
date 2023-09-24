package com.You.haveto.model

import com.google.gson.annotations.SerializedName

data class EmailCodeRequest (
    @SerializedName("email")
    val email: String
    )
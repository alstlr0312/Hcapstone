package com.example.capstone.SignUp.models

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("error") val error: List<Error>?,
    @SerializedName("data") val data: String? = null,

)

data class Error(
    @SerializedName("error") val error: String
)

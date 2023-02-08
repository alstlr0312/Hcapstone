package com.example.capstone.SignUp.models

import com.google.gson.annotations.SerializedName

data class PostSignUpRequest(
    @SerializedName("loginId") val loginId: String,
    @SerializedName("password") val password: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("field") val field: String? = null,

)

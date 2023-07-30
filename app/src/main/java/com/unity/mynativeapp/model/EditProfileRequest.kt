package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName

data class EditProfileRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("field") val field: String?=null,
    @SerializedName("memberId") val memberId: Int,
)

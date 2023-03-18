package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName
import okhttp3.RequestBody

data class LoginRequest(
	@SerializedName("loginId")
	val loginId: String,
	@SerializedName("password")
	val password: String
)
package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName

data class SignUpRequest (
	@SerializedName("loginId")
	val loginId: String,
	@SerializedName("password")
	val password: String,
	@SerializedName("username")
	val username: String,
	@SerializedName("email")
	val email: String,
)
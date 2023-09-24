package com.You.haveto.model

import com.google.gson.annotations.SerializedName

data class LoginData(
	@SerializedName("grantType") val grantType: String,
	@SerializedName("accessToken") val accessToken: String,
	@SerializedName("refreshToken") val refreshToken: String,
	@SerializedName("username") val username: String,
)
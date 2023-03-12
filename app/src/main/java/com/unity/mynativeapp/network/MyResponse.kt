package com.unity.mynativeapp.network

import com.google.gson.annotations.SerializedName

data class MyResponse<T>(
	@SerializedName("status")
	val status: Int,
	@SerializedName("error")
	val error: String? = null,
	@SerializedName("data")
	val data: T? = null,
)
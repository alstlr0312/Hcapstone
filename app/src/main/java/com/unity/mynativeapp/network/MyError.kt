package com.unity.mynativeapp.network

import com.google.gson.annotations.SerializedName


data class MyError<T>(
	@SerializedName("status")
	val status: Int,
	@SerializedName("error")
	val error: T? = null,
)

data class MyErrorList(
	@SerializedName("status")
	val status: Int,
	@SerializedName("error")
	val error: List<MyErrorItem>? = null,
)

data class MyErrorItem(val error: String)

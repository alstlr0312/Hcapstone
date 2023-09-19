package com.unity.mynativeapp.network

import com.google.gson.annotations.SerializedName


open class MyError<T>(
	@SerializedName("status")
	val status: Int,
	@SerializedName("error")
	val error: T? = null,
)

package com.unity.mynativeapp.network

import com.google.gson.annotations.SerializedName

open class MyResponse<T>(
	@SerializedName("status")
	val status: Int,
	@SerializedName("data")
	val data: T? = null,
)


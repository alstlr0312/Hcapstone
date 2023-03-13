package com.unity.mynativeapp.model

data class omePageResponse(
	val status: Int,
	val data: ResultHomePage,
	val error: Error ?= null
)


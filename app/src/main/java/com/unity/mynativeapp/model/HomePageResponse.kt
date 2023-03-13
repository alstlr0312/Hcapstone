package com.unity.mynativeapp.model

data class HomePageResponse(
	val status: Int,
	val data: ResultHomePage,
	val error: Error ?= null
)


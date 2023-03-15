package com.unity.mynativeapp.model

import java.time.LocalDate

data class HomePageResponse(
	val status: Int,
	val data: ResultHome,
	val error: Error ?= null
)

data class ResultHome(
	val calenders: List<CalenderItem>,
	val monthlyPercentage: Int,
	val today: LocalDate
)

data class CalenderItem(
	val exerciseDate: LocalDate? = null,
	val dailyPercentage: Int = -1,
)


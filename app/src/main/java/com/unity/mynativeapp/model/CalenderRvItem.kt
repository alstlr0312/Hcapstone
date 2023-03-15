package com.unity.mynativeapp.model

import com.unity.mynativeapp.model.DateItem
import java.time.LocalDate


data class CalenderRvItem(
	val exerciseDate: LocalDate? = null,
	var isSelectedDay: Boolean ?= false,
	var dailyPercentage: Int = -1,
)
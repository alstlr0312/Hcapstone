package com.unity.mynativeapp.model

import com.unity.mynativeapp.model.DateItem


data class CalenderRvItem(
	val date: DateItem?= null,
	var selectedDay: Boolean ?= false,
	var challenge: Int ? = null,
	var memo: String ?= null
)
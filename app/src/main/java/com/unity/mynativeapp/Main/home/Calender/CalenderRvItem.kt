package com.unity.mynativeapp.Main.home.Calender

import com.unity.mynativeapp.util.DateItem


data class CalenderRvItem(
    val date: DateItem?= null,
    var selectedDay: Boolean ?= false,
    var challenge: Int ? = null,
    var memo: String ?= null
)
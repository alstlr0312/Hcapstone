package com.unity.mynativeapp.Main.home.Calender

import com.unity.mynativeapp.util.DateItem
import java.time.LocalDate


data class CalenderRvItem(
    val date: LocalDate?= null,
    var isSelectedDay: Boolean ?= false,
    var percentage: Int ? = null,
    var diary: Boolean ?= false
)
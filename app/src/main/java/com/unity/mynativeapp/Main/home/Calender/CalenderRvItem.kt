package com.unity.mynativeapp.Main.home.Calender

import com.unity.mynativeapp.util.DateItem
import java.time.LocalDate


data class CalenderRvItem(
    val exerciseDate: LocalDate? = null,
    var isSelectedDay: Boolean ?= false,
    val dailyPercentage: Int = -1,
    )
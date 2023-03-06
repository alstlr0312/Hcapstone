package com.unity.mynativeapp.Main.home.Calender
import java.time.LocalDate


data class CalenderRvItem(
    val exerciseDate: LocalDate? = null,
    var isSelectedDay: Boolean ?= false,
    var dailyPercentage: Int = -1,
)
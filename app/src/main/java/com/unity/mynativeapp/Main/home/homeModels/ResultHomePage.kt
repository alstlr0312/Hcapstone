package com.example.capstone.Main.home.homeModels

import java.time.LocalDate
import java.util.*

data class ResultHomePage(
    val calender: MutableList<DayItem>,
    val dailyChallenge: DailyChallenge,
    val monthlyProgress: Int
)

data class DayItem(
    val date: LocalDate ?= null,
    var selected: Boolean ?= false,
    val challenge: Int ?= null,
    val memo: Boolean ?= false
)

data class DailyChallenge(
    val aerobic: Int,
    val unAerobic: Int,
    val total: Int
)

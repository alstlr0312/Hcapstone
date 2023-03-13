package com.unity.mynativeapp.model

import java.time.LocalDate

data class ResultHomePage(
    val calenders: List<CalenderItem>,
    val monthlyPercentage: Int,
    val today: LocalDate
)

data class CalenderItem(
    val exerciseDate: LocalDate? = null,
    val dailyPercentage: Int = -1,
)
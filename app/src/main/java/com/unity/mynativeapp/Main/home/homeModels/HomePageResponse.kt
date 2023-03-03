package com.example.capstone.Main.home.homeModels

import java.time.LocalDate

data class HomePageResponse(
    val status: Int,
    val error: String? = null,
    val data: ResultHome? = null,
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
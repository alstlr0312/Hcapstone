package com.unity.mynativeapp.model



data class HomeResponse(
    val calenders: List<Calender>,
    val monthlyPercentage: Int,
    val today: String
)

data class Calender(
    val dailyPercentage: Int,
    val exerciseDate: String
)

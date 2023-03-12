package com.unity.mynativeapp.model

import java.util.*

data class diarydateResponse(
    val status: Int,
    val data: Data
)
data class Data(
    val today: String,
    val calendars: List<Calendar>,
    val monthlyPercentage: Int
)
data class Calendar(
    val exerciseDate: String,
    val dailyPercentage: Int
)
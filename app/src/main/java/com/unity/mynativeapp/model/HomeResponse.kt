package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

/*
data class HomeResponse(
    @SerializedName("calenders") val calenders: List<DayItem>,
    @SerializedName("monthlyPercentage") val monthlyPercentage: Int,
    @SerializedName("today") val today: String
)

data class DayItem(
    @SerializedName("exerciseDate") val exerciseDate: LocalDate,
    @SerializedName("dailyPercentage") val dailyPercentage: Int
    )
*/
data class HomeResponse(
    val calenders: List<Calender>,
    val monthlyPercentage: Int,
    val today: String
)

data class Calender(
    val dailyPercentage: Int,
    val exerciseDate: String
)



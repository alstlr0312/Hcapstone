package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class HomeResponse(
    @SerializedName("calenders") val calenders: List<CalenderItem>,
    @SerializedName("monthlyPercentage") val monthlyPercentage: Int,
    @SerializedName("today") val today: LocalDate
)

data class CalenderItem(
    @SerializedName("exerciseDate") val exerciseDate: LocalDate? = null,
    @SerializedName("dailyPercentage") val dailyPercentage: Int = -1,
    )

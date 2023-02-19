package com.example.capstone.Main.home.homeModels

import com.unity.mynativeapp.Main.home.Calender.CalenderRvItem


data class ResultHomePage(
    val calender: MutableList<CalenderRvItem>,
    val dailyChallenge: DailyChallenge,
    val monthlyProgress: Int
)



data class DailyChallenge(
    val aerobic: Int,
    val unAerobic: Int,
    val total: Int
)
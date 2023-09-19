package com.unity.mynativeapp.model

import java.time.LocalDate


data class CalenderRvItem(
    val exerciseDate: LocalDate?= null,
    var isSelectedDay: Boolean ?= false,
    var dailyPercentage: Int = -1,
    val diaryId: Int = -1,
    )
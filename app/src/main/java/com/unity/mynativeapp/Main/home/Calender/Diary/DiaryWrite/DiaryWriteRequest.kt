package com.unity.mynativeapp.Main.home.Calender.Diary.DiaryWrite

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv.exerciseInfo

data class DiaryWriteRequest(
    @SerializedName("exerciseInfo") val exerciseInfo: List<exerciseInfo>,
    val review: String,
    val exerciseDate: String
)

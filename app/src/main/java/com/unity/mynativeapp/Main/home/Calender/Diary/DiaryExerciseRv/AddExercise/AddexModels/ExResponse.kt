package com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv.AddExercise.AddexModels

import com.google.gson.annotations.SerializedName

data class ExResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("error") val error: List<Error>?,
    @SerializedName("data") val data: String? = null,

    )

data class Error(
    @SerializedName("error") val error: String
)

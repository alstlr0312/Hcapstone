package com.unity.mynativeapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiaryWriteRequest(
    @SerializedName("exerciseInfo") val exerciseInfo: List<DiaryExerciseRvItem>,
    val review: String,
    val exerciseDate: String
)

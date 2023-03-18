package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName
import com.unity.mynativeapp.model.DiaryExerciseRvItem

data class DiaryWriteRequest(

    @SerializedName("exerciseInfo") val exerciseInfo: List<DiaryExerciseRvItem>,
    val review: String,
    val exerciseDate: String,
)


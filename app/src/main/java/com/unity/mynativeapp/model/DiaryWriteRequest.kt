package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName
import com.unity.mynativeapp.model.DiaryExerciseRvItem

data class DiaryWriteRequest(

    @SerializedName("exerciseInfo") val exerciseInfo: List<DiaryExerciseRvItem>,
    @SerializedName("review") val review: String,
    @SerializedName("exerciseDate") val exerciseDate: String,
)



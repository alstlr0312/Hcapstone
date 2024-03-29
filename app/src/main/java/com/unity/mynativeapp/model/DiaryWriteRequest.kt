package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName

data class DiaryWriteRequest(

    @SerializedName("exerciseInfo") val exerciseInfo: List<ExerciseItem>,
    @SerializedName("review") val review: String,
    @SerializedName("exerciseDate") val exerciseDate: String,
)



package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName
import com.unity.mynativeapp.model.DiaryExerciseRvItem

data class DiaryExerciseRvItem(
    @SerializedName("exerciseName") val exerciseName: String,
    @SerializedName("reps") val reps: Int? = null,
    @SerializedName("exSetCount") var exSetCount: Int ?= null,
    @SerializedName("cardio") var cardio: Boolean ?= true,
    @SerializedName("cardioTime")var cardioTime: Int ?= null,
    @SerializedName("bodyPart")var bodyPart: String,
    @SerializedName("finished") var finished: Boolean = false
)

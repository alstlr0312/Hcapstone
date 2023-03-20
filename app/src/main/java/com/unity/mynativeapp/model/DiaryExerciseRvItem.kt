package com.unity.mynativeapp.model

data class DiaryExerciseRvItem(
    var exerciseName: String,
    @SerializedName("reps") val reps: Int? = null,
    var exSetCount: Int ?= null,
    var cardio: Boolean ?= true,
    var cardioTime: Int ?= null,
    var bodyPart: String,
    var finished: Boolean = false,
)

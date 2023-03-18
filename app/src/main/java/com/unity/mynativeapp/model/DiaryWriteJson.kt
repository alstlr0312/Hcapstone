package com.unity.mynativeapp.model

data class DiaryWriteJson(
    val exerciseDate: String,
    val exerciseInfo: List<ExerciseInfo>,
    val review: String
)

data class ExerciseInfo(
    val bodyPart: String,
    val cardio: Boolean,
    val cardioTime: Int? = null,
    val exSetCount: Int? = null,
    val exerciseName: String,
    val finished: Boolean,
    val reps: Int? = null
)
package com.unity.mynativeapp.Main.home.Calender.Diary.DiaryWrite

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv.DiaryExerciseRvItem

data class DiaryWriteRequest(

    @SerializedName("exerciseInfo") val exerciseInfo: List<DiaryExerciseRvItem>,
    val review: String,
    val exerciseDate: String,
)

//data class ExerciseInfo(
//    val bodyPart: String,
//    val cardio: Boolean,
//    val cardioTime: Int,
//    val exSetCount: Int,
//    val exerciseName: String,
//    val finished: Boolean,
//    val reps: Int
//)
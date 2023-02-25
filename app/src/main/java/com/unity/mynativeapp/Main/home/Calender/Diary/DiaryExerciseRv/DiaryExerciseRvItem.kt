package com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv

data class DiaryExerciseRvItem(
    var exerciseName: String,
    var reps: Int ?= 0,
    var exSetCount: Int ?= 0,
    var cardio: Boolean ?= true,
    var cardioTime: Int ?= 0,
    var bodyPart: String,
    var finished: Boolean = false,

    var isClickable: Boolean ?= true,

    )

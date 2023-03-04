package com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv.AddExercise.AddexModels

import com.google.gson.annotations.SerializedName

data class ExData(
    var message: datainfo
)

data class datainfo(
    var exerciseDate : String? = null,
    var review       : String? = null,
    var exerciseInfo : ArrayList<ExerciseInfo> = arrayListOf(),
    var dateTime     : DateTime?               = DateTime(),
    var mediaList    : ArrayList<String>       = arrayListOf()
)

data class ExerciseInfo (
    var exerciseName : String?  = null,
    var reps         : Int?     = null,
    var exSetCount   : Int?     = null,
    var cardio       : Boolean? = null,
    var cardioTime   : Int?     = null,
    var bodyPart     : String?  = null,
    var finished     : Boolean? = null

)
data class DateTime (

    var createdAt  : String? = null,
    var canceledAt : String? = null,
    var updatedAt  : String? = null

)


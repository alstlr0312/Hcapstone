package com.unity.mynativeapp.Main.home.Calender.Diary.DiaryModels

import com.google.gson.annotations.SerializedName

data class DiaryData (
    @SerializedName("exerciseDate" ) var exerciseDate : String?                 = null,
    @SerializedName("review"       ) var review       : String?                 = null,
    @SerializedName("exerciseInfo" ) var exerciseInfo : ArrayList<ExerciseInfo> = arrayListOf(),
    @SerializedName("dateTime"     ) var dateTime     : DateTime?               = DateTime(),
    @SerializedName("mediaList"    ) var mediaList    : ArrayList<String>       = arrayListOf()
        )

data class ExerciseInfo (

    @SerializedName("exerciseName" ) var exerciseName : String?  = null,
    @SerializedName("reps"         ) var reps         : Int?     = null,
    @SerializedName("exSetCount"   ) var exSetCount   : Int?     = null,
    @SerializedName("cardio"       ) var cardio       : Boolean? = null,
    @SerializedName("cardioTime"   ) var cardioTime   : Int?     = null,
    @SerializedName("bodyPart"     ) var bodyPart     : String?  = null,
    @SerializedName("finished"     ) var finished     : Boolean? = null

)
data class DateTime (

    @SerializedName("createdAt"  ) var createdAt  : String? = null,
    @SerializedName("canceledAt" ) var canceledAt : String? = null,
    @SerializedName("updatedAt"  ) var updatedAt  : String? = null

)
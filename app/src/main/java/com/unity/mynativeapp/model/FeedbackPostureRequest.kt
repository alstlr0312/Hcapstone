package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName

data class FeedbackPostureRequest(

    @SerializedName("exerciseName")
    val exerciseName: String,
    @SerializedName("muscleName")
    val muscleName: String,
)

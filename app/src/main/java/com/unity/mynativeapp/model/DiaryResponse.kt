package com.unity.mynativeapp.model

import android.net.Uri
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class DiaryResponse(
    @SerializedName("review") val review: String,
    @SerializedName("exerciseDate") val exerciseDate: String,
    @SerializedName("exerciseInfo") val exerciseInfo: List<ExerciseItem>,
    @SerializedName("dateTime") val dateTime: List<DateItem>,
    @SerializedName("mediaList") val mediaList: List<Uri>
)

data class ExerciseItem(
    @SerializedName("exerciseName") val exerciseName: String? = null,
    @SerializedName("reps") val reps: Int? = null,
    @SerializedName("exSetCount") val exSetCount: Int ?= null,
    @SerializedName(" cardio") val cardio : Boolean ?= true,
    @SerializedName("cardioTime") val cardioTime: Int ?= null,
    @SerializedName("bodyPart") val bodyPart: String,
    @SerializedName("finished") val finished: Boolean = false,
)

data class DateItem(
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("canceledAt") val canceledAt: String? = null,
    @SerializedName("updatedAt") val updatedAt: Int ?= null,
)
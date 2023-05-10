package com.unity.mynativeapp.model

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class DiaryResponse(
    @SerializedName("diaryId") val diaryId: Int,
    @SerializedName("review") val review: String,
    @SerializedName("exerciseDate") val exerciseDate: String,
    @SerializedName("exerciseInfo") val exerciseInfo: List<ExerciseItem>,
    @SerializedName("dateTime") val dateTime: List<DateItem>,
    @SerializedName("mediaList") val mediaList: List<String> // Uri 대신 String 타입으로 변경
) {
    val mediaUriList: List<Uri> // 변환된 Uri 리스트를 저장할 프로퍼티 추가
        get() = mediaList.map { Uri.parse(it) } // 각 문자열을 Uri.parse() 메서드를 사용하여 Uri 객체로 변환하여 반환
}
data class ExerciseItem(
    @SerializedName("exerciseName") val exerciseName: String,
    @SerializedName("reps") val reps: Int? = 0,
    @SerializedName("exSetCount") val exSetCount: Int ?= 0,
    @SerializedName(" cardio") val cardio : Boolean,
    @SerializedName("cardioTime") val cardioTime: Int ?= 0,
    @SerializedName("bodyPart") val bodyPart: String,
    @SerializedName("finished") val finished: Boolean,
)

data class DateItem(
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("canceledAt") val canceledAt: String? = null,
    @SerializedName("updatedAt") val updatedAt: Int ?= null,
)
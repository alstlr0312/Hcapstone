package com.unity.mynativeapp.model

import android.net.Uri
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

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
    @SerializedName("exerciseName") var exerciseName: String,
    @SerializedName("reps") var reps: Int? = 0,
    @SerializedName("exSetCount") var exSetCount: Int ?= 0,
    @SerializedName("cardio") var cardio : Boolean,
    @SerializedName("cardioTime") var cardioTime: Int ?= 0,
    @SerializedName("bodyPart") var bodyPart: String,
    @SerializedName("finished") var finished: Boolean,
)

data class DateItem(
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("canceledAt") val canceledAt: String? = null,
    @SerializedName("updatedAt") val updatedAt: Int ?= null,
)

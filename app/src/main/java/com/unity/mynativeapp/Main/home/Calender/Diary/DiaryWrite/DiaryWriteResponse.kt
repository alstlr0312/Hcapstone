package com.unity.mynativeapp.Main.home.Calender.Diary.DiaryWrite

import com.google.gson.annotations.SerializedName
import lombok.Getter
import lombok.Setter

@Getter
@Setter
data class DiaryWriteResponse(
     val status: Int,
     val error: String?,
     val data: String?,
)
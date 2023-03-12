package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName

data class DiaryWriteResponse(
     val status: Int,
     val error: String?,
     val data: String?,
)
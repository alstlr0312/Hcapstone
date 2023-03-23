package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName

data class DiaryWriteResponse(

    @SerializedName("data") val data: String,
)


package com.You.haveto.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.Multipart

data class MediaResponse(
    @SerializedName("media") val media: Multipart,
)
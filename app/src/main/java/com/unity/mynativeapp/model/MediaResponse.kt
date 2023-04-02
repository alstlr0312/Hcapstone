package com.unity.mynativeapp.model

import android.net.Uri
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class MediaResponse(
    @SerializedName("media") val review: MultipartBody,
)
package com.unity.mynativeapp.model

import android.media.Image
import android.net.Uri
import android.widget.ImageView
import com.google.gson.annotations.SerializedName
import com.unity.mynativeapp.network.RetrofitClient.gson
import okhttp3.MultipartBody
import retrofit2.http.Multipart

data class MediaResponse(
    @SerializedName("media") val media: Multipart,
)
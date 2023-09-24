package com.You.haveto.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PostWriteRequest(
    @SerializedName("title") val title: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("postType") val postType: String?,
    @SerializedName("workOutCategory") val workOutCategory: String?
): Serializable


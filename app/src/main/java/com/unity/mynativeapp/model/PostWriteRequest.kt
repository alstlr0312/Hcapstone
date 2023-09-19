package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName

class PostWriteRequest(
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("postType") val postType: String,
    @SerializedName("workOutCategory") val workOutCategory: String
)


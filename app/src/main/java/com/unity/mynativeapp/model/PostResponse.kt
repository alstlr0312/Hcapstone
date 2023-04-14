package com.unity.mynativeapp.model

import android.net.Uri
import com.google.gson.annotations.SerializedName

class PostResponse(
    @SerializedName("username") val username: String,
    @SerializedName("profileImage") val profileImage: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("mediaList") val mediaList: List<String>, // Uri 대신 String 타입으로 변경,
    @SerializedName("views") val views: Int,
    @SerializedName("likeCount") val likeCount: Int,
    @SerializedName("likePressed") val likePressed: Boolean,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("postType") val postType: String,
    @SerializedName("workOutCategory") val workOutCategory: String,
    @SerializedName("comments") val comments: String,
    @SerializedName("mine") val mine: Boolean

)
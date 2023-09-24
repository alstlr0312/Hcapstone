package com.You.haveto.model

import com.google.gson.annotations.SerializedName

class PostResponse(
    @SerializedName("postListDto") val postListDto: List<PostItem>,
    @SerializedName("hasNext") val hasNext: Boolean,
    @SerializedName("isFirst") val isFirst: Boolean
)

data class PostItem(
    @SerializedName("username") val username: String,
    @SerializedName("postType") val postType: String,
    @SerializedName("workOutCategory") val workOutCategory: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("title") val title: String,
    @SerializedName("postId") val postId: Int,
    @SerializedName("mediaListCount") val mediaListCount: Int,
    @SerializedName("likeCount") val likeCount: Int,
    @SerializedName("views") val views: Int,
    @SerializedName("commentCount") val commentCount: Int
)
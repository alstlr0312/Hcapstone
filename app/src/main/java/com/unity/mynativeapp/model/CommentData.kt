package com.unity.mynativeapp.model

data class CommentData(
    val commentId: Int?,
    val childCount: Int,
    val commentContext: String,
    val createdAt: String,
    val profileImage: String?=null, // url
    val username: String
)
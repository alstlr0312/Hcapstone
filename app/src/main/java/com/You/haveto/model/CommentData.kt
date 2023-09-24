package com.You.haveto.model

data class CommentData(
    val commentId: Int?,
    var childCount: Int,
    val commentContext: String,
    val createdAt: String,
    val profileImage: String?=null, // url
    val username: String
)
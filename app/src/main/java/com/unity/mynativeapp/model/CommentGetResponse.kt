package com.unity.mynativeapp.model

data class CommentGetResponse(
    val commentListDto: List<CommentDto>,
    val hasNext: Boolean,
    val isFirst: Boolean
)

data class CommentDto(
    val childCount: Int,
    val commentContext: String,
    val createdAt: String,
    val profileImage: String,
    val username: String
)
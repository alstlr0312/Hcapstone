package com.unity.mynativeapp.model

data class GetCommentResponse(
    val commentListDto: List<CommentDto>,
    val hasNext: Boolean,
    val isFirst: Boolean
)

data class CommentDto(
    val childCount: Int,
    val commentContext: String,
    val createdAt: String,
    val profileImage: Int, // 서버 연결시 String으로 변경
    val username: String
)
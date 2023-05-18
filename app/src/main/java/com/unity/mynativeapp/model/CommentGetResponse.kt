package com.unity.mynativeapp.model

data class CommentGetResponse(
    val commentListDto: List<CommentData>,
    val hasNext: Boolean,
    val isFirst: Boolean
)


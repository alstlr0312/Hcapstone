package com.You.haveto.model

data class CommentGetResponse(
    val commentListDto: List<CommentData>,
    val hasNext: Boolean,
    val isFirst: Boolean,
    var parentId: Int?=null,
)


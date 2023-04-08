package com.unity.mynativeapp.model

import java.time.LocalDateTime

data class PostListResponse(
    val data: List<PostListDto>
)

data class PostListDto(
    val username: String,
    val postType: String,
    val workOutCategory: String,
    val createdAt: String,
    val title: String,
    val views: Int ?= 0,
    val likeCount: Int ?= 0,
    val commentCount: Int ?= 0,
    val mediaListCount: Int ?= 0,
)

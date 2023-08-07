package com.unity.mynativeapp.model


data class MemberPageResponse(
    val commentCount: Int?,
    val createdAt: String,
    val email: String?,
    val field: String?,
    val postCount: Int,
    val profileImage: String?,
    val username: String
)
package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName

data class CommentWriteRequest(

    @SerializedName("postId")
    val postId: Int,
    @SerializedName("commentContent")
    val commentContent: String,
    @SerializedName("parentId")
    val parentId: Int?,
)

package com.unity.mynativeapp.Main.community

data class PostingRvItem(
    val userName: String,
    val postingTxt: String,
    val heartNum: Int,
    val commentNum: Int,
    val profileImg: Int ?= null,
    val postingImg: Int ?= null,
    )


/*
data class PostingRvItem(
    val postingId: Int,
    val profileImg: String,
    val userName: String,
    val postingImg: ArrayList<String>,
    val postingVideo: ArrayList<String>,
    val heartNum: Int,
    val commentNum: Int,
)
 */
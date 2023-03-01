package com.unity.mynativeapp.Main.community

import com.unity.mynativeapp.R
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime

data class PostingRvItem(
    val userName: String,
    val postTitle: String,
    val postDate: LocalDateTime,
    val heartNum: Int ?= 0,
    val commentNum: Int ?= 0,
    val mediaNum: Int ?= 0,
    val profileImg: Int ?= R.drawable.ic_profile_photo_base,
    //val profileImg: URL ?= null
    )

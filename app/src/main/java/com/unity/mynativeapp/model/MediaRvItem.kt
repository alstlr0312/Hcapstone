package com.unity.mynativeapp.model

import android.net.Uri

data class MediaRvItem(
    val viewType : Int, // image or video
    val uri: Uri?,
    val url: String?
    )

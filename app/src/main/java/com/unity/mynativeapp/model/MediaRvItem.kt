package com.unity.mynativeapp.model

import android.net.Uri

data class MediaRvItem(
    val viewType : Int,
    val uri: Uri?,
    val byteArray: ByteArray?,
    )

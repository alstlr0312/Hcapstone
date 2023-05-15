package com.unity.mynativeapp.model

import android.graphics.Bitmap
import android.net.Uri

data class MediaRvItem(
    val viewType : Int,
    val uri: Uri?,
    val bitmap: Bitmap?,
    )

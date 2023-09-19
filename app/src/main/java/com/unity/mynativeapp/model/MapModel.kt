package com.unity.mynativeapp.model

import com.naver.maps.map.overlay.Marker


data class MapModel(
    val Address: Double = 0.0,
    val TRDSTATENM: Double = 0.0,
    val DTLSTATENM: String = "", //등록 태그 or 경로 태그 구분
    val status: String = "",
    val location: String = "",
)
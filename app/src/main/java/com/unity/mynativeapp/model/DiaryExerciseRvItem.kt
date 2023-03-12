package com.unity.mynativeapp.model

data class DiaryExerciseRvItem(
    var isChecked: Boolean ?= false,
    var name: String,
    var numbers: Int ?= null,
    var sets: Int ?= null,
    var times: Int ?= null,
)

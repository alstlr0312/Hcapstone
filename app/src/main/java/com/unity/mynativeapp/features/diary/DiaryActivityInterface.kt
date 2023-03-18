package com.unity.mynativeapp.features.diary

import com.unity.mynativeapp.model.DiaryWriteResponse

interface DiaryActivityInterface {
    fun onPostLoginSuccess(response: DiaryWriteResponse)
    fun onPostLoginFailure(message: String)
}
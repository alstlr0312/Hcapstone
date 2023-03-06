package com.unity.mynativeapp.Main.home.Calender.Diary

import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryWrite.DiaryWriteResponse

interface DiaryActivityInterface {
    fun onPostLoginSuccess(response: DiaryWriteResponse)
    fun onPostLoginFailure(message: String)
}
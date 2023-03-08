package com.unity.mynativeapp.Main.home.Calender.Diary

import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryWrite.DiaryWriteResponse
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap


interface DiaryActivityInterface {
    fun onPostLoginSuccess(response: DiaryWriteResponse)
    fun onPostLoginFailure(message: String)


}
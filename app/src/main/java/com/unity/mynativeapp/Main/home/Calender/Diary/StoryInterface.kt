package com.unity.mynativeapp.Main.home.Calender.Diary


import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryWrite.DiaryWriteResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface StoryInterface {

    @Multipart
    @POST("diary/write")
    @Headers("Authorization:Bearer[ eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZCIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE2NzgyNjU5MTh9.Em0DZWWpU_oWjO3UKCWL10plSNMVCtkOMFQ2IsQZLe7QQfN5lC7OYaGkLnmSt70EllWQ0OL72mM-Rxlve_93iQ]")
    fun createDiary(
        @Part("writeDiaryDto") postData: MultipartBody.Builder,
       // @Body params: RequestBody
    ): Call<DiaryWriteResponse>
}

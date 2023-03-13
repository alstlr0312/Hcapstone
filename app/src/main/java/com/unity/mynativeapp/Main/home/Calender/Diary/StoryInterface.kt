package com.unity.mynativeapp.Main.home.Calender.Diary


import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryWrite.DiaryWriteResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface StoryInterface {

    @Multipart
    @POST("diary/write")
    @Headers("Authorization:Bearer[eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZCIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE2Nzg2ODQyNTB9.7pA2GO09bTzEeFJGTNUTRjWwJ6V_r3Kk_BQ5o-3sHtFBYfPDtegZn2af7xPfdCBk2h1gYa3oEFv2KJR6Ptsh5w]")
    fun createDiary(
        @Part("writeDiaryDto") writeDiaryDto : RequestBody
    ): Call<DiaryWriteResponse>

    @Multipart
    @POST("diary/write")
    @Headers("Authorization:Bearer[eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZCIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE2Nzg2ODQyNTB9.7pA2GO09bTzEeFJGTNUTRjWwJ6V_r3Kk_BQ5o-3sHtFBYfPDtegZn2af7xPfdCBk2h1gYa3oEFv2KJR6Ptsh5w]")
    fun createDiary2(
        @Part("writeDiaryDto") writeDiaryDto: HashMap<String, RequestBody>,
    ): Call<DiaryWriteResponse>
}

package com.unity.mynativeapp.Main.home.Calender.Diary


import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryWrite.DiaryWriteResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface StoryInterface {

    @FormUrlEncoded
    @Multipart
    @POST("diary/write")
    fun createDiary(
        @Header("Authorization") auth: String,
        @Part("writeDiaryDto") writeDiaryDto: String
    ): Call<DiaryWriteResponse>

    @Multipart
    @POST("diary/write")
    @Headers("Authorization:Bearer[eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZCIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE2Nzg2ODY3MTl9.cNFL280YXN0lNppPSarMBkjVoYpvApzUVQN_lhvL7JFYPM6M1ZOv4OAt51_5SG-vGTi8NCUrLq4Us7W8KJ41rw]")
    fun createDiary2(
        @Part("writeDiaryDto") writeDiaryDto: HashMap<String, RequestBody>,
    ): Call<DiaryWriteResponse>
}

package com.unity.mynativeapp.Main.home.Calender.Diary


import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryWrite.DiaryWriteResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface StoryInterface {

    @Multipart
    @POST("diary/write")
    @Headers("Authorization:Bearer[eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZCIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE2Nzg0Mjc4MzB9.VaOOfFD7PUjbtYRiuF1TdE8wqkeKLrAtg0m2BsV1BdmjF9AOJdx5W0FjJHqx_a5-OBtx5ujStm02w5JVFLEpBQ]")
    fun createDiary(
        @Part("writeDiaryDto") writeDiaryDto: MultipartBody.Builder,
    ): Call<DiaryWriteResponse>

    @Multipart
    @POST("diary/write")
    @Headers("Authorization:Bearer[eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZCIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE2Nzg0Mjc4MzB9.VaOOfFD7PUjbtYRiuF1TdE8wqkeKLrAtg0m2BsV1BdmjF9AOJdx5W0FjJHqx_a5-OBtx5ujStm02w5JVFLEpBQ]")
    fun createDiary2(
        @Part("writeDiaryDto") writeDiaryDto: HashMap<String, RequestBody>,
    ): Call<DiaryWriteResponse>
}

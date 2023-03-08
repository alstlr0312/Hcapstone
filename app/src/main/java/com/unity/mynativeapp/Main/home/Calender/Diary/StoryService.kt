package com.unity.mynativeapp.Main.home.Calender.Diary

import android.util.Log
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.ApplicationClass
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryWrite.DiaryWriteResponse
import okhttp3.MultipartBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StoryService(diaryActivity: DiaryActivity) {

    fun writeStory(rBody: MultipartBody.Builder) {
        val retrofit =
            Retrofit.Builder()
                .baseUrl(ApplicationClass.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val storyService = retrofit.create(StoryInterface::class.java)


        storyService.createDiary(rBody).enqueue(object : Callback<DiaryWriteResponse> {
                override fun onResponse(call: Call<DiaryWriteResponse>, response: Response<DiaryWriteResponse>)
                {
                    if(response.isSuccessful()) {
                        Log.d("ssssssssssssssssssss",  response.body().toString()+response.errorBody()+response.code())
                    }
                    else {
                        Log.d("red", response.body().toString()+response.errorBody())
                    }
                    val DiaryWriteResponse =
                    Log.d("ssssssssssssssssssss", response.code().toString())
                    Log.d("red", response.isSuccessful.toString())
                }

                override fun onFailure(call: Call<DiaryWriteResponse>, t: Throwable) {

                    Log.d("fffffffffffffffffff",  "fffffffffffff" )
                }
            })
    }
}
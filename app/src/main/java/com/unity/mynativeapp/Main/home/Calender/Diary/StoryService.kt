package com.unity.mynativeapp.Main.home.Calender.Diary

import android.util.Log
import com.unity.mynativeapp.ApplicationClass
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryWrite.DiaryWriteResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StoryService(diaryActivity: DiaryActivity) {

    fun writeStory(rBody: String) {
        val retrofit =
            Retrofit.Builder()
                .baseUrl(ApplicationClass.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val storyService = retrofit.create(StoryInterface::class.java)

        val accessToken = "Bearer["+ApplicationClass.sSharedPreferences.getString(ApplicationClass.X_ACCESS_TOKEN, null).toString()+"]"
        storyService.createDiary(accessToken,rBody).enqueue(object :
            Callback<DiaryWriteResponse> {
                override fun onResponse(call: Call<DiaryWriteResponse>, response: Response<DiaryWriteResponse>)
                {
                    Log.d("ssssssssssssssssssss",  rBody.toString())
                    if(response.isSuccessful()) {
                        Log.d("ssssssssssssssssssss",  response.errorBody().toString())
                        Log.d("ssssssssssssssssssss", response.code().toString())
                    }
                    else {
                        Log.d("s",  response.errorBody().toString())
                        Log.d("f", response.code().toString())
                    }
                    Log.d("ssssssssssssssssssss",  response.errorBody().toString())
                    Log.d("fffff", response.code().toString())

                }

                override fun onFailure(call: Call<DiaryWriteResponse>, t: Throwable) {

                }
            })
    }

    fun writeStory2(rBody: HashMap<String, RequestBody>) {
        val retrofit =
            Retrofit.Builder()
                .baseUrl(ApplicationClass.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val storyService = retrofit.create(StoryInterface::class.java)


        storyService.createDiary2(rBody).enqueue(object :
            Callback<DiaryWriteResponse> {
            override fun onResponse(call: Call<DiaryWriteResponse>, response: Response<DiaryWriteResponse>)
            {
                Log.d("ssssssssssssssssssss",  rBody.toString())
                if(response.isSuccessful()) {
                    Log.d("ssssssssssssssssssss",  response.errorBody().toString())
                    Log.d("ssssssssssssssssssss", response.code().toString())
                }
                else {
                    Log.d("s",  response.errorBody().toString())
                    Log.d("f", response.code().toString())
                }
                Log.d("ssssssssssssssssssss",  response.errorBody().toString())
                Log.d("fffff", response.code().toString())

            }

            override fun onFailure(call: Call<DiaryWriteResponse>, t: Throwable) {

            }
        })
    }

}
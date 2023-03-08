package com.unity.mynativeapp.Main.home.Calender.Diary

import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.ApplicationClass
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryWrite.DiaryWriteResponse
import okhttp3.*
import okhttp3.CacheControl.Companion.parse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class DiaryActivityService(val diaryActivityInterface: DiaryActivityInterface) {

    companion object{
        val DAIRY_WRTIE = "diary/write"
        val auth = ApplicationClass.AUTHORIZATION


    }

    fun tryPostDiaryWrite(requestBody: MultipartBody){

        val postRequest = Request.Builder()
            .url(ApplicationClass.API_URL + DAIRY_WRTIE)
            .post(requestBody)
            .build()


        ApplicationClass.okHttpClient.newCall(postRequest).enqueue(object : okhttp3.Callback {

            override fun onResponse(call: Call, response: okhttp3.Response) {

                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val data = gson.fromJson(body, DiaryWriteResponse::class.java)

                if (data != null) {
                    Log.d("diaryActivityService", data.toString() + " " + response.code)
                    diaryActivityInterface.onPostLoginSuccess(data)

                } else {
                    diaryActivityInterface.onPostLoginFailure(response.code.toString())
                }


            }
            override fun onFailure(call: Call, e: IOException) {
                diaryActivityInterface.onPostLoginFailure(e.message.toString())
            }
        })
    }










}
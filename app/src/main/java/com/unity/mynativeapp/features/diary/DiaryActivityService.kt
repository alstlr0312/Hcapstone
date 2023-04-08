package com.unity.mynativeapp.features.diary

import okhttp3.MultipartBody

class DiaryActivityService(val diaryActivityInterface: DiaryActivityInterface) {

    companion object{
        val DAIRY_WRTIE = "diary/write"
    }

    fun tryPostDiaryWrite(requestBody: MultipartBody){

        /*val postRequest = Request.Builder()
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
        })*/
    }


}
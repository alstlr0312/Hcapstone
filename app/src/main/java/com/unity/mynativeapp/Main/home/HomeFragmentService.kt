package com.unity.mynativeapp.Main.home


import android.util.Log
import com.example.capstone.Main.home.homeModels.HomePageResponse
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.ApplicationClass
import com.unity.mynativeapp.BaseResponse
import okhttp3.Request
import java.io.IOException

class HomeFragmentService(val homeFragmentInterface: HomeFragmentInterface){

    companion object {
        const val HOME_PAGE = "diary?date="
    }

    // 홈화면 요청
    fun tryGetHomePage(date: String){

        val getHomePageRequest = Request.Builder()
            .url(ApplicationClass.API_URL + HOME_PAGE + date).get().build()

        ApplicationClass.okHttpClient.newCall(getHomePageRequest).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val body = response.body?.string()
                var data = GsonBuilder().create().fromJson(body, BaseResponse::class.java)

                if(data.status == 200 || data.status == 400){
                    var successData = GsonBuilder().create().fromJson(body, HomePageResponse::class.java)
                    homeFragmentInterface.onGetHomePageSuccess(successData)
                }else{
                    Log.d("homeFragmentService", data.status.toString())
                }
            }
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                homeFragmentInterface.onGetHomePageFailure("통신 오류: "+e.message.toString())
                Log.d("homeFragmentService", "통신 오류: "+e.message.toString())
            }
        })
    }

}
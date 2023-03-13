package com.unity.mynativeapp.feature.home


import android.util.Log
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.model.HomePageResponse
import com.unity.mynativeapp.model.diarydateResponse
import com.unity.mynativeapp.network.RetrofitClient
import okhttp3.Request

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class HomeFragmentService(val homeFragmentInterface: HomeFragmentInterface) {


    // 홈화면 요청


    // 홈 화면 요청
    fun tryGetHomePage(date: String){
            RetrofitClient.getApiService().datePage(date).enqueue(object : Callback<diarydateResponse> {

                override fun onResponse(
                    call: Call<diarydateResponse>,
                    response: Response<diarydateResponse>
                ) {
                    if(response.isSuccessful){
                        Log.d("HomeFragmentService", response.body()?.data.toString())
                        homeFragmentInterface.onGetHomePageSuccess(response.body() as HomePageResponse)
                    }else{
                        Log.d("HomeFragmentService", "response is not successful")
                    }
                }

                override fun onFailure(call: Call<diarydateResponse>, t: Throwable) {
                    Log.d("HomeFragmentService", "response is not successful")
                }

            })
    }

}
package com.unity.mynativeapp.Main.home

import com.example.capstone.Main.home.homeModels.HomePageResponse
import retrofit2.Call
import retrofit2.http.*

interface HomeRetrofitInterface {

    // 홈 화면
    @GET("/home")
    fun getHomePage(@Query("userIdx") userIdx: Int) : Call<HomePageResponse>

}
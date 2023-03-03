package com.unity.mynativeapp.Main.home

import com.example.capstone.Main.home.homeModels.HomePageResponse
import okhttp3.MediaType
import okhttp3.RequestBody

interface HomeFragmentInterface {
    // 홈화면 요청
    fun onGetHomePageSuccess(response: HomePageResponse)
    fun onGetHomePageFailure(message: String)

}
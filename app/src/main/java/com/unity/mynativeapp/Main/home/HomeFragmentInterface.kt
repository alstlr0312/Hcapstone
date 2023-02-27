package com.unity.mynativeapp.Main.home

import com.example.capstone.Main.home.homeModels.HomePageResponse

interface HomeFragmentInterface {
    // 홈화면 요청
    fun onGetHomePageSuccess(response: HomePageResponse)
    fun onGetHomePageFailure(message: String)
}
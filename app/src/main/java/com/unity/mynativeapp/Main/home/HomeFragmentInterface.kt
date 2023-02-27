package com.unity.mynativeapp.Main.home

interface HomeFragmentInterface {
    // 홈화면 요청
    fun onGetHomePageSuccess(response: HomePageResponse)
    fun onGetHomePageFailure(message: String)
}

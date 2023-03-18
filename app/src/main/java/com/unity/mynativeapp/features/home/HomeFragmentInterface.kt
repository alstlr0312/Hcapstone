package com.unity.mynativeapp.features.home

import com.unity.mynativeapp.model.HomePageResponse

interface HomeFragmentInterface {
    // 홈화면 요청
    fun onGetHomePageSuccess(response: HomePageResponse)
    fun onGetHomePageFailure(message: String)
}

package com.unity.mynativeapp.feature.home

import com.unity.mynativeapp.model.HomePageResponse

interface HomeFragmentInterface {

    // 스토어 화면 요청
    fun onGetHomePageSuccess(response: HomePageResponse)
    fun onGetHomePageFailure(message: String)
}

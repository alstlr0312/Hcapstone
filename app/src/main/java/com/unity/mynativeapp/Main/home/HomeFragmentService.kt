package com.unity.mynativeapp.Main.home


import android.util.Log
import com.example.capstone.Main.home.homeModels.HomePageResponse
import com.unity.mynativeapp.ApplicationClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragmentService(val homeFragmentInterface: HomeFragmentInterface) {

    // 홈 화면 요청
    fun tryGetHomePage(userId: Int){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(HomeRetrofitInterface::class.java)
        homeRetrofitInterface.getHomePage(userId).enqueue(object : Callback<HomePageResponse> {
            override fun onResponse(
                call: Call<HomePageResponse>,
                response: Response<HomePageResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("HomeFragmentService", response.body()?.data.toString())
                    homeFragmentInterface.onGetHomePageSuccess(response.body() as HomePageResponse)
                }else{
                    Log.d("HomeFragmentService", "response is not successful")
                }
            }

            override fun onFailure(call: Call<HomePageResponse>, t: Throwable) {
                homeFragmentInterface.onGetHomePageFailure(t.message ?: "통신 오류")
            }

        })
    }

}
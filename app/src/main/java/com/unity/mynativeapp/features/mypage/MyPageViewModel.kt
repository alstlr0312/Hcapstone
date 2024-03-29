package com.unity.mynativeapp.features.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unity.mynativeapp.model.MemberPageResponse
import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class MyPageViewModel: ViewModel() {
    val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    val _myPageData = MutableLiveData<MemberPageResponse?>()
    val myPageData: LiveData<MemberPageResponse?> = _myPageData

    val _logout = MutableLiveData<Boolean>()
    val logout: LiveData<Boolean> = _logout
    fun myPageInfo(username: String) {
        _loading.postValue(true)
        getMyPageInfoAPI(username)
    }

    private fun getMyPageInfoAPI(username: String) { // 마이페이지 정보 가져오기
        RetrofitClient.getApiService().getMemberPage(username).enqueue(object :
            Callback<MyResponse<MemberPageResponse>> {
            override fun onResponse(call: Call<MyResponse<MemberPageResponse>>, response: Response<MyResponse<MemberPageResponse>>) {
                _loading.postValue(false)
                val code = response.code()
                Log.d(TAG, code.toString())

                when(code) {
                    200 -> { // 성공
                        val data = response.body()?.data
                        Log.d(TAG, data.toString())
                        data?.let { _myPageData.postValue(data) }
                    }
                    400 -> { // 잘못된 유저 이름으로 요청
                        _myPageData.postValue(null)
                    }
                    401 -> {// refresh 토큰 만료
                        _logout.postValue(true)
                    }
                    else -> {
                        Log.d(TAG, "$code")
                    }
                }
            }
            override fun onFailure(call: Call<MyResponse<MemberPageResponse>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                _loading.postValue(false)

            }
        })
    }

    companion object {
        const val TAG = "MyPageViewModel"
    }
}
package com.unity.mynativeapp.features.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.model.HomeResponse
import com.unity.mynativeapp.network.MyError
import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _homeData = MutableLiveData<HomeResponse?>()
    val homeData: LiveData<HomeResponse?> = _homeData

    private val _logout = MutableLiveData<Boolean>(false)
    val logout: LiveData<Boolean> = _logout

    fun home(date: String) {

        _loading.postValue(true)

        getHomeAPI(date)
    }

    private fun getHomeAPI(date: String) {
        RetrofitClient.getApiService().getHome(date).enqueue(object : Callback<MyResponse<HomeResponse>> {
            override fun onResponse(call: Call<MyResponse<HomeResponse>>, response: Response<MyResponse<HomeResponse>>) {
                _loading.postValue(false)

                val code = response.code()
                Log.d(TAG, code.toString())

                when(code) {
                    200 -> { // 다이어리 목록 있음
                        val data = response.body()?.data
                        Log.d(TAG, data.toString())

                        data?.let {
                            _homeData.postValue(data)
                        }
                    }
                    401 -> {// refresh 토큰 만료
                        _logout.postValue(true)
                    }
                    400 -> { // 다이어리 목록 없음
                        _homeData.postValue(null)
                    }
                    else -> {
                        Log.d(TAG, "$code")
                    }
                }
            }


            override fun onFailure(call: Call<MyResponse<HomeResponse>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                _loading.postValue(false)

            }
        })
    }

    companion object {
        const val TAG = "HomeViewModel"
    }
}
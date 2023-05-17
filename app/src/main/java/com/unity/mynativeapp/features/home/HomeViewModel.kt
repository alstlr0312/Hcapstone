package com.unity.mynativeapp.features.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.features.diary.DiaryViewModel
import com.unity.mynativeapp.model.HomeResponse
import com.unity.mynativeapp.network.MyError
import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.network.RetrofitClient
import com.unity.mynativeapp.util.DELETE_COMPLETE
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(): ViewModel() {
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _homeData = MutableLiveData<HomeResponse?>()
    val homeData: LiveData<HomeResponse?> = _homeData

    private val _logout = MutableLiveData<Boolean>(false)
    val logout: LiveData<Boolean> = _logout

    private val _diaryDeleteData = MutableLiveData<Int?>()
    val diaryDeleteData: LiveData<Int?> = _diaryDeleteData


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
                    400 -> { // 다이어리 목록 없음
                        _homeData.postValue(null)
                    }
                    401 -> {// refresh 토큰 만료
                        _logout.postValue(true)
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

    fun diaryDelete(num: Int){
        _loading.postValue(true)
        diaryDeleteAPI(num)
    }

    private fun diaryDeleteAPI(num: Int){
        RetrofitClient.getApiService().patchDiaryDelete(num).enqueue(object :
            Callback<MyResponse<Int>> {
            override fun onResponse(call: Call<MyResponse<Int>>, response: Response<MyResponse<Int>>) {
                _loading.postValue(false)

                val code = response.code()
                if(code == 200){ // 다이어리 삭제 성공
                    val data = response.body()?.data
                    _toastMessage.postValue(DELETE_COMPLETE)
                    _diaryDeleteData.postValue(data)

                }else if(code == 401) { // 존재하지 않는 유저
                    // 재로그인
                    Log.d(DiaryViewModel.TAG, "$code 존재하지 않는 유저")
                    _logout.postValue(true)
                }
                else if(code == 400){
                    Log.d(DiaryViewModel.TAG, "$code 존재하지 않는 다이어리입니다.")
                }else if(code == 500){
                    Log.d(DiaryViewModel.TAG, "$code I/O 서버 오류")
                }else{
                    Log.d(DiaryViewModel.TAG, "$code")
                }

            }

            override fun onFailure(call: Call<MyResponse<Int>>, t: Throwable) {
                Log.e(DiaryViewModel.TAG, "Error: ${t.message}")
                _loading.postValue(false)

            }
        })
    }

    companion object {
        const val TAG = "HomeViewModel"
    }
}
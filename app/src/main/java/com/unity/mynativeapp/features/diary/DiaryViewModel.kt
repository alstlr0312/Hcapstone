package com.unity.mynativeapp.features.diary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unity.mynativeapp.model.DiaryResponse
import com.unity.mynativeapp.model.DiaryWriteResponse
import com.unity.mynativeapp.model.HomeResponse
import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.network.RetrofitClient
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiaryViewModel: ViewModel() {

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _diaryWriteSuccess = MutableLiveData<Boolean>()
    val diaryWriteSuccess: LiveData<Boolean> = _diaryWriteSuccess

    private val _diaryData = MutableLiveData<DiaryResponse?>()
    val diaryData: LiveData<DiaryResponse?> = _diaryData

    fun diaryWrite(body: RequestBody, body1: MutableList<MultipartBody.Part>) {

        _loading.postValue(true)

        postDiaryWrite(body,body1)
    }

    private fun postDiaryWrite(body: RequestBody, body1: MutableList<MultipartBody.Part>) {
        RetrofitClient.getApiService().postDiaryWrite(body,body1).enqueue(object :
            Callback<MyResponse<DiaryWriteResponse>> {
            override fun onResponse(call: Call<MyResponse<DiaryWriteResponse>>, response: Response<MyResponse<DiaryWriteResponse>>) {
                _loading.postValue(false)

                val code = response.code()
                if(code == 201){ // 다이어리 작성 성공
                    val data = response.body()?.data
                    _diaryWriteSuccess.postValue(true)

                }else if(code == 401) { // 존재하지 않는 유저
                    // 재로그인
                    Log.d(TAG, "$code 존재하지 않는 유저")
                }
                else if(code == 400){
                    Log.d(TAG, "$code   ")
                }else if(code == 415){
                    Log.d(TAG, "$code 잘못된 Content-Type")
                }else if(code == 500){
                    Log.d(TAG, "$code 파일 입출력 오류")
                }else{
                    Log.d(TAG, "$code")
                }

            }


            override fun onFailure(call: Call<MyResponse<DiaryWriteResponse>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                _loading.postValue(false)

            }
        })
    }

    fun home(date: String) {

        _loading.postValue(true)

        getDiaryAPI(date)
    }

    private fun getDiaryAPI(date: String) {
        RetrofitClient.getApiService().getDiary(date).enqueue(object :
            Callback<MyResponse<DiaryResponse>> {
            override fun onResponse(call: Call<MyResponse<DiaryResponse>>, response: Response<MyResponse<DiaryResponse>>) {
                _loading.postValue(false)

                val code = response.code()
                Log.d(TAG, code.toString())

                when(code) {
                    200 -> { // 다이어리 목록 있음
                        val data = response.body()?.data
                        Log.d(TAG, data.toString())
                        data?.let {
                            _diaryData.postValue(data)
                        }
                    }
                    400 -> {// 다이어리 목록 없음
                         _diaryData.postValue(null)
                    }

                    else -> {
                        Log.d(TAG, "$code")
                    }

                }
            }


            override fun onFailure(call: Call<MyResponse<DiaryResponse>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                _loading.postValue(false)
            }
        })
    }

    companion object {
        const val TAG = "DiaryViewModel"
    }
}
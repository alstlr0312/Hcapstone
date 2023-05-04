package com.unity.mynativeapp.features.postwrite

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.unity.mynativeapp.model.PostResponse
import com.unity.mynativeapp.model.PostWriteResponse
import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.network.RetrofitClient
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostwriteViewModel: ViewModel() {

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _postWriteSuccess = MutableLiveData<Boolean>()
    val postWriteSuccess: LiveData<Boolean> = _postWriteSuccess

    private val _postData = MutableLiveData<PostWriteResponse?>()
    val postData: LiveData<PostWriteResponse?> = _postData


    fun postWrite(body: RequestBody, body1: MutableList<MultipartBody.Part>) {

        _loading.postValue(true)

        postWriteApi(body, body1)
    }

    private fun postWriteApi(body: RequestBody, body1: MutableList<MultipartBody.Part>) {
        RetrofitClient.getApiService().postPostWrite(body, body1).enqueue(object :
            Callback<MyResponse<PostWriteResponse>> {
            override fun onResponse(
                call: Call<MyResponse<PostWriteResponse>>,
                response: Response<MyResponse<PostWriteResponse>>
            ) {
                // _loading.postValue(false)

                val code = response.code()
                if (code == 201) { // 다이어리 작성 성공
                    val data = response.body()?.data
                    _postWriteSuccess.postValue(true)

                } else if (code == 401) { // 존재하지 않는 유저
                    // 재로그인
                    Log.d(TAG, "$code 존재하지 않는 유저")
                } else if (code == 400) {
                    Log.d(TAG, "$code   ")
                } else if (code == 415) {
                    Log.d(TAG, "$code 잘못된 Content-Type")
                } else if (code == 500) {
                    Log.d(TAG, "$code 파일 입출력 오류")
                } else {
                    Log.d(TAG, "$code")
                }

            }


            override fun onFailure(call: Call<MyResponse<PostWriteResponse>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                _loading.postValue(false)

            }
        })
    }



}

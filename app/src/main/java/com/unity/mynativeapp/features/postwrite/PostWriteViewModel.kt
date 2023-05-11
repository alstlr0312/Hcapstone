package com.unity.mynativeapp.features.postwrite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.network.RetrofitClient
import com.unity.mynativeapp.util.POST_WRITE_COMPLETE
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostWriteViewModel: ViewModel() {

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _logout = MutableLiveData<Boolean>(false)
    val logout: LiveData<Boolean> = _logout

    private val _postWriteSuccess = MutableLiveData<Boolean>()
    val postWriteSuccess: LiveData<Boolean> = _postWriteSuccess


    fun postWrite(body: RequestBody, body1: MutableList<MultipartBody.Part>) {

        _loading.postValue(true)

        postWriteApi(body, body1)
    }

    private fun postWriteApi(body: RequestBody, body1: MutableList<MultipartBody.Part>) {
        RetrofitClient.getApiService().postPostWrite(body, body1).enqueue(object :
            Callback<MyResponse<String>> {
            override fun onResponse(
                call: Call<MyResponse<String>>,
                response: Response<MyResponse<String>>
            ) {
                _loading.postValue(false)

                val code = response.code()
                when(code) {
                    201 -> { // 게시글 작성 성공
                        val data = response.body()?.data
                        _toastMessage.postValue(POST_WRITE_COMPLETE)
                        _postWriteSuccess.postValue(true)
                    }
                    401 -> {
                        Log.d(TAG, "$code 존재하지 않는 유저")
                        _logout.postValue(true)
                    }
                    400 -> Log.d(TAG, "$code   ")
                    415 -> Log.d(TAG, "$code 잘못된 Content-Type")
                    500 -> Log.d(TAG, "$code 파일 입출력 오류")
                    else -> Log.d(TAG, "$code")
                }

            }


            override fun onFailure(call: Call<MyResponse<String>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                _loading.postValue(false)

            }
        })
    }

    companion object {
        const val TAG = "PostWriteViewModel"
    }

}

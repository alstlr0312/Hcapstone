package com.unity.mynativeapp.features.community

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unity.mynativeapp.model.PostResponse
import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostViewModel: ViewModel() {

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _postWriteSuccess = MutableLiveData<Boolean>()
    val postWriteSuccess: LiveData<Boolean> = _postWriteSuccess

    private val _postData = MutableLiveData<PostResponse?>()
    val postData: LiveData<PostResponse?> = _postData


    private val _mediaData = MutableLiveData<ResponseBody?>()
    val mediaData: MutableLiveData<ResponseBody?> = _mediaData


    fun community(data: String, s: String, i: Int, i1: Int) {

        _loading.postValue(true)

        getPostAPI(data, s,i,i1)
    }

    private fun getPostAPI(data: String, s: String, i: Int, i1: Int) {
        RetrofitClient.getApiService().getPost(data,s,i,i1).enqueue(object :
            Callback<MyResponse<PostResponse>> {
            override fun onResponse(call: Call<MyResponse<PostResponse>>, response: Response<MyResponse<PostResponse>>) {
                _loading.postValue(false)

                val code = response.code()
                Log.d(TAG, code.toString())

                when(code) {


                    200 -> { // 다이어리 목록 있음
                        val data = response.body()?.data
                        Log.d(TAG, data.toString())
                        data?.let {
                            _postData.postValue(data)
                        }
                    }
                    400 -> {// 다이어리 목록 없음
                        _postData.postValue(null)
                    }

                    else -> {
                        Log.d(TAG, "$code")
                    }
                }
            }


            override fun onFailure(call: Call<MyResponse<PostResponse>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                _loading.postValue(false)
            }
        })
    }
    /*fun media(num: Int) {

        _loading.postValue(true)

        getmediaAPI(num)
    }

    private fun getmediaAPI(num: Int) {
        RetrofitClient.getApiService2().getMedia(num).enqueue(object :
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                _loading.postValue(false)
                if (response.isSuccessful) {
                    val imageBytes = response.body()
                    _mediaData.postValue(imageBytes)
                } else {
                    // 응답이 실패한 경우 처리하는 코드 작성
                }

            }


            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                _loading.postValue(false)
            }
        })
    }

    companion object {
        const val TAG = "DiaryViewModel"
    }*/
}
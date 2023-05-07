package com.unity.mynativeapp.features.postdetail

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unity.mynativeapp.model.PostDetailResponse
import com.unity.mynativeapp.model.PostResponse
import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDetailViewModel : ViewModel() {

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _postWriteSuccess = MutableLiveData<Boolean>()
    val postWriteSuccess: LiveData<Boolean> = _postWriteSuccess

    private val _postDetailData = MutableLiveData<PostDetailResponse?>()
    val postData: LiveData<PostDetailResponse?> = _postDetailData


    private val _mediaData = MutableLiveData<ResponseBody?>()
    val mediaData: MutableLiveData<ResponseBody?> = _mediaData


    fun PostDetail(i: Int) {

        _loading.postValue(true)

        getPostDetailAPI(i)
    }

    private fun getPostDetailAPI(i: Int) {
        RetrofitClient.getApiService().getDetailPost(i).enqueue(object :
            Callback<MyResponse<PostDetailResponse>> {
            override fun onResponse(
                call: Call<MyResponse<PostDetailResponse>>,
                response: Response<MyResponse<PostDetailResponse>>
            ) {
                _loading.postValue(false)

                val code = response.code()
                Log.d(ContentValues.TAG, code.toString())
                when (code) {

                    200 -> { // 다이어리 목록 있음
                        val data = response.body()?.data
                        Log.d(ContentValues.TAG, data.toString())
                        data?.let {
                            _postDetailData.postValue(data)
                        }
                    }
                    400 -> {// 다이어리 목록 없음
                        _postDetailData.postValue(null)
                    }

                    else -> {
                        Log.d(ContentValues.TAG, "$code")
                    }
                }
            }


            override fun onFailure(call: Call<MyResponse<PostDetailResponse>>, t: Throwable) {
                Log.e(ContentValues.TAG, "Error: ${t.message}")
                _loading.postValue(false)
            }
        })
    }
}
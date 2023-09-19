package com.unity.mynativeapp.features.community

import android.content.ContentValues
import android.content.ContentValues.TAG
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

class PostViewModel: ViewModel() {

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _logout = MutableLiveData<Boolean>()
    val logout: LiveData<Boolean> = _logout

    private val _postWriteSuccess = MutableLiveData<Boolean>()
    val postWriteSuccess: LiveData<Boolean> = _postWriteSuccess

    private val _postData = MutableLiveData<PostResponse?>()
    val postData: LiveData<PostResponse?> = _postData


    private val _mediaData = MutableLiveData<ResponseBody?>()
    val mediaData: MutableLiveData<ResponseBody?> = _mediaData


    fun community(type: String?=null, category: String?=null, page: Int?=null, size: Int?=null) {

        _loading.postValue(true)

        getPostAPI(type, category, page, size)
    }

    private fun getPostAPI(type: String?, category: String?, page: Int?, size: Int?) {
        RetrofitClient.getApiService().getPost(type,category,page,size).enqueue(object : Callback<MyResponse<PostResponse>> {
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
                    401 -> _logout.postValue(true)
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

}
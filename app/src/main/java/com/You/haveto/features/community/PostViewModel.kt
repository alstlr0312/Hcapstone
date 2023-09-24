package com.You.haveto.features.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.You.haveto.model.PostResponse
import com.You.haveto.network.MyResponse
import com.You.haveto.network.RetrofitClient
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

    private val _postData = MutableLiveData<PostResponse?>()
    val postData: LiveData<PostResponse?> = _postData


    fun community(type: String?, category: String?, username: String?, page: Int?, size: Int?) {

        _loading.postValue(true)

        getPostAPI(type, category, username, page, size)
    }

    private fun getPostAPI(type: String?, category: String?, username: String?, page: Int?, size: Int?) {
        RetrofitClient.getApiService().getPost(type,category,username,page,size).enqueue(object : Callback<MyResponse<PostResponse>> {
            override fun onResponse(call: Call<MyResponse<PostResponse>>, response: Response<MyResponse<PostResponse>>) {
                _loading.postValue(false)

                val code = response.code()
                Log.d(TAG, code.toString())

                when(code) {
                    200 -> { // 게시글 목록 조회 성공
                        val data = response.body()?.data
                        Log.d(TAG, data.toString())
                        data?.let {
                            _postData.postValue(data)
                        }
                    }
                    400 -> {
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

    companion object{
        const val TAG="PostViewModelLog"
    }

}
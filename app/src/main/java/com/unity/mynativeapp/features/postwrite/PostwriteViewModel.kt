package com.unity.mynativeapp.features.postwrite

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.features.postdetail.PostDetailViewModel
import com.unity.mynativeapp.model.PostDetailResponse
import com.unity.mynativeapp.network.*
import com.unity.mynativeapp.network.util.POST_EDIT_COMPLETE

import com.unity.mynativeapp.network.util.POST_WRITE_COMPLETE

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Dictionary

class PostWriteViewModel: ViewModel() {

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _logout = MutableLiveData<Boolean>(false)
    val logout: LiveData<Boolean> = _logout

    private val _postWriteSuccess = MutableLiveData<Boolean>()
    val postWriteSuccess: LiveData<Boolean> = _postWriteSuccess

    private val _postEditData = MutableLiveData<PostDetailResponse?>()
    val postEditData: MutableLiveData<PostDetailResponse?> = _postEditData


    // 게시글 수정 요청
    fun postEdit(postId: Int, postDto: RequestBody, files: MutableList<MultipartBody.Part>) {

        _loading.postValue(true)

        postEditApi(postId, postDto, files)
    }

    private fun postEditApi(postId: Int, postDto: RequestBody, files: MutableList<MultipartBody.Part>) {
        RetrofitClient.getApiService().postPatchEdit(postId, postDto, files).enqueue(object :
            Callback<MyResponse<String>> {
            override fun onResponse(
                call: Call<MyResponse<String>>,
                response: Response<MyResponse<String>>
            ) {
                _loading.postValue(false)

                val code = response.code()
                when(code) {
                    200 -> { // 게시글 수정 성공
                        val data = response.body()?.data
                        _toastMessage.postValue(POST_EDIT_COMPLETE)
                        _postWriteSuccess.postValue(true)
                    }
                    400 -> {
                        val body = response.errorBody()?.string()
                        val data = GsonBuilder().create().fromJson(body, MyErrorList::class.java)
                        Log.d(TAG, "$code  ${data.error.toString()}")
                        _toastMessage.postValue(data.error?.get(0)?.error.toString())
                    }
                    else -> {
                        val body = response.errorBody()?.string()
                        val data = GsonBuilder().create().fromJson(body, MyError::class.java)
                        Log.d(TAG, "$code  ${data.error.toString()}")
                        _toastMessage.postValue(data.error.toString())
                        if(code == 401) _logout.postValue(true)

                    }
                }
            }
            override fun onFailure(call: Call<MyResponse<String>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                _loading.postValue(false)

            }
        })
    }

    // 게시글 작성
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
                    400 -> {
                        val body = response.errorBody()?.string()
                        val data = GsonBuilder().create().fromJson(body, MyErrorList::class.java)
                        Log.d(TAG, "$code  ${data.error.toString()}")
                        _toastMessage.postValue(data.error?.get(0)?.error.toString())
                    }
                    else -> {
                        val body = response.errorBody()?.string()
                        val data = GsonBuilder().create().fromJson(body, MyError::class.java)
                        Log.d(TAG, "$code  ${data.error.toString()}")
                        _toastMessage.postValue(data.error.toString())
                        if(code == 401) _logout.postValue(true)

                    }
                }

            }


            override fun onFailure(call: Call<MyResponse<String>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                _loading.postValue(false)

            }
        })
    }

    ///// 수정할 게시글 데이터 조회
    fun getPostEditData(postId: Int) {

        _loading.postValue(true)

        getPostEditDataAPI(postId)
    }

    private fun getPostEditDataAPI(postId: Int) {
        RetrofitClient.getApiService().getDetailPost(postId).enqueue(object :
            Callback<MyResponse<PostDetailResponse>> {
            override fun onResponse(
                call: Call<MyResponse<PostDetailResponse>>,
                response: Response<MyResponse<PostDetailResponse>>
            ) {
                _loading.postValue(false)

                val code = response.code()
                when (code) {
                    200 -> { // 수정할 게시글 데이터 조회 성공
                        val data = response.body()?.data
                        Log.d(TAG, data.toString())
                        data?.let {
                            _postEditData.postValue(data)
                        }
                    }
                    else -> { // 400: 존재하지 않는 게시글입니다. // 401: 존재하지 않는 유저입니다. //  403: 본인 게시글이 아닙니다.
                        val body = response.errorBody()?.string()
                        val data = GsonBuilder().create().fromJson(body, MyError::class.java)
                        Log.d(TAG, "$code  ${data.error.toString()}")
                        _toastMessage.postValue(data.error.toString())
                        _postEditData.postValue(null)
                        if(code == 401) _logout.postValue(true)
                    }
                }
            }

            override fun onFailure(call: Call<MyResponse<PostDetailResponse>>, t: Throwable) {
                Log.e(ContentValues.TAG, "Error: ${t.message}")
                _loading.postValue(false)
            }
        })
    }
    companion object {
        const val TAG = "PostWriteViewModel"
    }

}

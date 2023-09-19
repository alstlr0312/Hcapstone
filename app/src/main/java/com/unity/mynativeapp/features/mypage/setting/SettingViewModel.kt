package com.unity.mynativeapp.features.mypage.setting

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.config.BaseActivity.Companion.DISMISS_LOADING
import com.unity.mynativeapp.config.BaseActivity.Companion.SHOW_LOADING
import com.unity.mynativeapp.config.BaseActivity.Companion.SHOW_TEXT_LOADING
import com.unity.mynativeapp.features.login.LoginViewModel
import com.unity.mynativeapp.features.mypage.MyPageViewModel
import com.unity.mynativeapp.model.EditProfileRequest
import com.unity.mynativeapp.model.LoginData
import com.unity.mynativeapp.model.LoginRequest
import com.unity.mynativeapp.network.MyError
import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.network.RetrofitClient
import com.unity.mynativeapp.network.util.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.regex.Pattern

class SettingViewModel: MyPageViewModel(){

    // 회원 탈퇴
    private val _MemberDeleteSuccess = MutableLiveData<Boolean>()
    val MemberDeleteSuccess: LiveData<Boolean> = _MemberDeleteSuccess

    private val _loading = MutableLiveData<Int>()
    val loading2: LiveData<Int> = _loading

    // 회원 탈퇴 요청
    fun memberDelete(password: String) {

        _loading.postValue(SHOW_TEXT_LOADING)

        memberDeleteAPI(password)
    }

    private fun memberDeleteAPI(password: String) {
        RetrofitClient.getApiService().patchMemberDelete(password).enqueue(object : Callback<MyResponse<Int>> {
            override fun onResponse(call: Call<MyResponse<Int>>, response: Response<MyResponse<Int>>) {
                _loading.postValue(DISMISS_LOADING)

                val code = response.code()
                Log.d(TAG, code.toString())

                when(code) {
                    200 -> { // 회원 탈퇴 성공
                        val data = response.body()?.data
                        Log.d(TAG, data.toString())
                        data?.let {
                            _MemberDeleteSuccess.postValue(true)
                        }
                    }
                    else -> {
                        val data = GsonBuilder().create().fromJson(response.errorBody()?.string(), MyError::class.java)
                        val error = data.error.toString()
                        _toastMessage.postValue(error)
                        when(code){
                            400 -> {
                                _MemberDeleteSuccess.postValue(false)
                            }
                            401 -> {
                                _logout.postValue(true)
                            }
                            else -> {
                                _MemberDeleteSuccess.postValue(true)
                                Log.d(TAG, "$code")
                            }

                        }
                    }
                }
            }
            override fun onFailure(call: Call<MyResponse<Int>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                _loading.postValue(DISMISS_LOADING)
            }
        })
    }

    companion object{
        val TAG = "ProfileViewModel"
    }
}
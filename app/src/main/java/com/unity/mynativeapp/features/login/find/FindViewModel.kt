package com.unity.mynativeapp.features.login.find

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.features.signup.SignUpViewModel
import com.unity.mynativeapp.model.CheckResponse
import com.unity.mynativeapp.model.CheckRequest
import com.unity.mynativeapp.model.LoginData
import com.unity.mynativeapp.model.LoginRequest
import com.unity.mynativeapp.network.MyError
import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.network.RetrofitClient
import com.unity.mynativeapp.network.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindViewModel: ViewModel() {

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _findData = MutableLiveData<String>()
    val findData: LiveData<String> = _findData

    private val _findError = MutableLiveData<String>()
    val findError: LiveData<String> = _findError

    private val _checkSuccess = MutableLiveData(false)
    val checkSuccess: LiveData<Boolean> = _checkSuccess

    // 이메일 인증
    private var emailCheckCode = ""
    fun emailCheck(email: String) {
        _loading.postValue(true)
        postEmailCheckAPI(email)
    }

    private fun postEmailCheckAPI(email: String) {
        RetrofitClient.getApiService().email(CheckRequest(email)).enqueue(object : Callback<MyResponse<CheckResponse>> {
            override fun onResponse(call: Call<MyResponse<CheckResponse>>, response: Response<MyResponse<CheckResponse>>) {
                _loading.postValue(false)
                val code = response.code()
                if (code == 200) {
                    val data = response.body()?.data ?: return
                    emailCheckCode = data.code
                    _checkSuccess.postValue(true)
                    _toastMessage.postValue(EMAIL_CODE_SEND_SUCCESS)
                }else{ // 400, 1) 잘못된 인증 코드, 2) 존재하지 않는 유저
                    val body = response.errorBody()?.string()
                    val data = GsonBuilder().create().fromJson(body, MyError::class.java)
                    _checkSuccess.postValue(false)
                    _toastMessage.postValue(data.error.toString())
                }
            }


            override fun onFailure(call: Call<MyResponse<CheckResponse>>, t: Throwable) {
                Log.e(SignUpViewModel.TAG, "Error: ${t.message}")
                _loading.postValue(false)
            }
        })
    }

    // 아이디 찾기
    fun findId(checkCode: String) {
        _loading.postValue(true)
        findIdAPI(checkCode)
    }

    private fun findIdAPI(checkCode: String) {
        RetrofitClient.getApiService().getFindId(checkCode).enqueue(object : Callback<MyResponse<String>> {
            override fun onResponse(call: Call<MyResponse<String>>, response: Response<MyResponse<String>>) {
                _loading.postValue(false)
                val code = response.code()
                if (code == 200) {
                    val data = response.body()?.data ?: return
                    _findData.postValue(data)
                }else if(code == 400){ // 400, 1) 잘못된 인증 코드, 2) 존재하지 않는 유저
                    val body = response.errorBody()?.string()
                    val data = GsonBuilder().create().fromJson(body, MyError::class.java)
                    _toastMessage.postValue(data.error.toString())
                    _findError.postValue(data.error.toString())
                }
            }
            override fun onFailure(call: Call<MyResponse<String>>, t: Throwable) {
                Log.e(SignUpViewModel.TAG, "Error: ${t.message}")
                _loading.postValue(false)
            }
        })
    }

    // 비밀번호 찾기
    fun findPw(checkCode: String) {
        _loading.postValue(true)
        findPwAPI(checkCode)
    }

    private fun findPwAPI(checkCode: String) {
        RetrofitClient.getApiService().patchFindPw(checkCode).enqueue(object : Callback<MyResponse<String>> {
            override fun onResponse(call: Call<MyResponse<String>>, response: Response<MyResponse<String>>) {
                _loading.postValue(false)
                val code = response.code()
                if (code == 200) {
                    val data = response.body()?.data ?: return
                    _findData.postValue(data)
                }else if(code == 400){ // 400, 1) 잘못된 인증 코드, 2) 존재하지 않는 유저
                    val body = response.errorBody()?.string()
                    val data = GsonBuilder().create().fromJson(body, MyError::class.java)
                    _toastMessage.postValue(data.error.toString())
                    _findError.postValue(data.error.toString())
                }
            }
            override fun onFailure(call: Call<MyResponse<String>>, t: Throwable) {
                Log.e(SignUpViewModel.TAG, "Error: ${t.message}")
                _loading.postValue(false)
            }
        })
    }

    companion object {
        const val TAG = "FindViewModel"
    }
}
package com.unity.mynativeapp.features.login.find

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.features.signup.SignUpViewModel
import com.unity.mynativeapp.model.EmailCodeResponse
import com.unity.mynativeapp.model.EmailCodeRequest
import com.unity.mynativeapp.model.FindPwRequest
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

    private val _getEmailCodeSuccess = MutableLiveData(false)
    val getEmailCodeSuccess: LiveData<Boolean> = _getEmailCodeSuccess

    private val _emailCheckSuccess = MutableLiveData(false)
    val emailCheckSuccess: LiveData<Boolean> = _emailCheckSuccess

    // 이메일 인증코드 요청
    private var emailCheckCode = ""
    fun emailCode(email: String) {
        _loading.postValue(true)
        emailCodeAPI(email)
    }

    private fun emailCodeAPI(email: String) {
        RetrofitClient.getApiService().emailCode(EmailCodeRequest(email)).enqueue(object : Callback<MyResponse<EmailCodeResponse>> {
            override fun onResponse(call: Call<MyResponse<EmailCodeResponse>>, response: Response<MyResponse<EmailCodeResponse>>) {
                _loading.postValue(false)
                val code = response.code()
                if (code == 200) {
                    val data = response.body()?.data ?: return
                    emailCheckCode = data.code
                    _getEmailCodeSuccess.postValue(true)
                    _toastMessage.postValue(EMAIL_CODE_SEND_SUCCESS)
                }else{ // 400, 1) 잘못된 인증 코드, 2) 존재하지 않는 유저
                    val body = response.errorBody()?.string()
                    val data = GsonBuilder().create().fromJson(body, MyError::class.java)
                    _getEmailCodeSuccess.postValue(false)
                    _toastMessage.postValue(data.error.toString())
                }
            }
            override fun onFailure(call: Call<MyResponse<EmailCodeResponse>>, t: Throwable) {
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

    // 이메일 인증 코드 확인(for 비밀 번호 변경)
    fun emailCheck(code: String) {
        _loading.postValue(true)
        emailCheckAPI(code)
    }

    private fun emailCheckAPI(code: String) {
        RetrofitClient.getApiService().emailCheck(code).enqueue(object : Callback<MyResponse<String>> {
            override fun onResponse(call: Call<MyResponse<String>>, response: Response<MyResponse<String>>) {
                _loading.postValue(false)
                val code = response.code()
                if (code == 200) {
                    val data = response.body()?.data ?: return
                    _emailCheckSuccess.postValue(true)
                }else{ // 400
                    val body = response.errorBody()?.string()
                    val data = GsonBuilder().create().fromJson(body, MyError::class.java)
                    _emailCheckSuccess.postValue(false)
                    _toastMessage.postValue(data.error.toString())
                }
            }
            override fun onFailure(call: Call<MyResponse<String>>, t: Throwable) {
                Log.e(SignUpViewModel.TAG, "Error: ${t.message}")
                _loading.postValue(false)
            }
        })
    }

    // 비밀번호 변경
    fun findPw(pwRequest: FindPwRequest) {
        _loading.postValue(true)

        findPwAPI(pwRequest)
    }

    private fun findPwAPI(pwRequest: FindPwRequest) {
        RetrofitClient.getApiService().patchFindPw(pwRequest).enqueue(object : Callback<MyResponse<String>> {
            override fun onResponse(call: Call<MyResponse<String>>, response: Response<MyResponse<String>>) {
                _loading.postValue(false)
                val code = response.code()
                if (code == 200) { // 비밀번호 변경 성공
                    val data = response.body()?.data ?: return
                    _toastMessage.postValue(CHANGE_PASSWORD_SUCCESS)
                    _findData.postValue(data)
                }else if(code == 400){ // 400
                    val body = response.errorBody()?.string()
                    val data = GsonBuilder().create().fromJson(body, MyError::class.java)
                    _toastMessage.postValue(data.error.toString())
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
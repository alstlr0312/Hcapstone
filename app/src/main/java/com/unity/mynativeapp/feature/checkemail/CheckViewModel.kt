package com.unity.mynativeapp.feature.checkemail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.model.CheckData
import com.unity.mynativeapp.model.CheckRequest
import com.unity.mynativeapp.model.LoginData
import com.unity.mynativeapp.model.SignUpRequest
import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.network.RetrofitClient
import com.unity.mynativeapp.util.ID_EMPTY_ERROR
import com.unity.mynativeapp.util.SIGNUP_SUCCESS
import com.unity.mynativeapp.util.X_ACCESS_TOKEN
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckViewModel : ViewModel() {
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _loginSuccess = MutableLiveData<Boolean>(false)
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()

    fun check(email: String) {

        // Email이 비어있을경우
        if (email.isEmpty()) {
            _toastMessage.postValue(ID_EMPTY_ERROR)
            return
        }

        // Password가 비어있을 경우

        _loading.postValue(true)

        postLoginAPI(email)
    }

    private fun postLoginAPI(email: String) {
        RetrofitClient.getApiService().email(CheckRequest(email)).enqueue(object :
            Callback<MyResponse<CheckData>> {
            override fun onResponse(call: Call<MyResponse<CheckData>>, response: Response<MyResponse<CheckData>>) {
                _loading.postValue(false)
                val code = response.code()
                val data = response.body()?.data
                if (code == 200) {
                    if (data == null) return

                    MyApplication.prefUtil.setString("code", data.code)
                    _toastMessage.postValue(SIGNUP_SUCCESS)
                }
            }


            override fun onFailure(call: Call<MyResponse<CheckData>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                _loading.postValue(false)
            }
        })
    }

    companion object {
        const val TAG = "LoginViewModel"
    }
}
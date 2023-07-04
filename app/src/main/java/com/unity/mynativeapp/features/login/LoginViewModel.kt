package com.unity.mynativeapp.features.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.model.LoginData
import com.unity.mynativeapp.model.LoginRequest
import com.unity.mynativeapp.network.MyError
import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.network.RetrofitClient
import com.unity.mynativeapp.network.util.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

	private val _toastMessage = MutableLiveData<String>()
	val toastMessage: LiveData<String> = _toastMessage

	private val _loading = MutableLiveData<Boolean>()
	val loading: LiveData<Boolean> = _loading

	private val _loginSuccess = MutableLiveData<Boolean>(false)
	val loginSuccess: LiveData<Boolean> = _loginSuccess


	fun login(id: String, password: String) {

		// Email이 비어있을경우
		if (id.isEmpty()) {
			_toastMessage.postValue(ID_EMPTY_ERROR)
			return
		}

		// Password가 비어있을 경우
		if (password.isEmpty()) {
			_toastMessage.postValue(PW_EMPTY_ERROR)
			return
		}

		_loading.postValue(true)

		postLoginAPI(id, password)
	}

	private fun postLoginAPI(id: String, password: String) {
		RetrofitClient.getApiService().login(LoginRequest(id, password)).enqueue(object : Callback<MyResponse<LoginData>> {
			override fun onResponse(call: Call<MyResponse<LoginData>>, response: Response<MyResponse<LoginData>>) {
				_loading.postValue(false)


				val code = response.code()

				if(code == 200){ // 로그인 성공
					val data = response.body()?.data

					if(data != null){
						MyApplication.prefUtil.setString(X_ACCESS_TOKEN, data.accessToken)
						MyApplication.prefUtil.setString(X_REFRESH_TOKEN, data.refreshToken)
						MyApplication.prefUtil.setString("username", data.username)
						_toastMessage.postValue(LOGIN_SUCCESS)
						_loginSuccess.postValue(true)
					}

				}else if(code == 401){// 로그인 실패
					val body = response.errorBody()?.string()
					val data = GsonBuilder().create().fromJson(body, MyError::class.java)
					_toastMessage.postValue(data.error.toString())
				}

			}


			override fun onFailure(call: Call<MyResponse<LoginData>>, t: Throwable) {
				Log.e(TAG, "Error: ${t.message}")
				_loading.postValue(false)

			}
		})
	}


	companion object {
		const val TAG = "LoginViewModel"
	}
}
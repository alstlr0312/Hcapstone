package com.unity.mynativeapp.feature.login

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.feature.BaseActivity
import com.unity.mynativeapp.model.LoginData
import com.unity.mynativeapp.model.LoginRequest
import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.network.RetrofitClient
import com.unity.mynativeapp.util.ID_EMPTY_ERROR
import com.unity.mynativeapp.util.PW_EMPTY_ERROR
import com.unity.mynativeapp.util.SIGNUP_SUCCESS
import com.unity.mynativeapp.util.X_ACCESS_TOKEN
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

	val shouldStartActivity = MutableLiveData<Boolean>(false)

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

	private fun postLoginAPI(email: String, password: String) {
		RetrofitClient.getApiService().login(LoginRequest(email, password)).enqueue(object : Callback<MyResponse<LoginData>> {
			override fun onResponse(call: Call<MyResponse<LoginData>>, response: Response<MyResponse<LoginData>>) {
				_loading.postValue(false)
				val code = response.code()
				val data = response.body()?.data
				if (code == 201) {
					data?.let {
						if (data.accessToken.isEmpty()) {
							MyApplication.prefUtil.setString(X_ACCESS_TOKEN, data.accessToken)
						}
					}
					shouldStartActivity.postValue(true)

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
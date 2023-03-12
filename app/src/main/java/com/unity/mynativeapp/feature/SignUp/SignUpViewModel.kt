package com.unity.mynativeapp.feature.SignUp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.feature.login.LoginViewModel
import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.model.SignUpRequest
import com.unity.mynativeapp.network.RetrofitClient
import com.unity.mynativeapp.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class SignUpViewModel : ViewModel() {

	private val _toastMessage = MutableLiveData<String>()
	val toastMessage: LiveData<String> = _toastMessage

	private val _loading = MutableLiveData<Boolean>()
	val loading: LiveData<Boolean> = _loading

	private val emailPattern = android.util.Patterns.EMAIL_ADDRESS
	private val pwPattern = Pattern.compile("^(?=.*([a-z].*[A-Z])|([A-Z].*[a-z]))(?=.*[0-9])(?=.*[\$@\$!%*#?&.])[A-Za-z[0-9]\$@\$!%*#?&.]{8,20}\$")

	fun signup(loginId: String, password: String, passwordCheck: String, email: String, username: String) {
		if (loginId.isEmpty()) {
			_toastMessage.postValue(ID_EMPTY_ERROR)
			return
		}

		if (password.isEmpty()) {
			_toastMessage.postValue(PW_EMPTY_ERROR)
			return
		}

		if (password != passwordCheck) {
			_toastMessage.postValue(PW_NOT_SAME_ERROR)
			return
		}

		if (email.isEmpty()) {
			_toastMessage.postValue(EMAIL_EMPTY_ERROR)
			return
		}

		if (username.isEmpty()) {
			_toastMessage.postValue(NICKNAME_EMPTY_ERROR)
			return
		}

		RetrofitClient.getApiService().signup(SignUpRequest(loginId, password, email, username)).enqueue(object : Callback<MyResponse<String>> {
			override fun onResponse(call: Call<MyResponse<String>>, response: Response<MyResponse<String>>) {
				_loading.postValue(false)

				val code = response.code()
				val data = response.body()?.data

				if (code == 201) {
					if (data == null) return
					MyApplication.prefUtil.setString("username", data)
					_toastMessage.postValue(SIGNUP_SUCCESS)

				}
			}

			override fun onFailure(call: Call<MyResponse<String>>, t: Throwable) {
				Log.e(LoginViewModel.TAG, "Error: ${t.message}")
				_loading.postValue(false)
			}
		})
	}
}
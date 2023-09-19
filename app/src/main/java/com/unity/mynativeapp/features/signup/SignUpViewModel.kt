package com.unity.mynativeapp.features.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.features.login.LoginViewModel
import com.unity.mynativeapp.model.*
import com.unity.mynativeapp.network.MyError
import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.network.RetrofitClient
import com.unity.mynativeapp.network.util.*

import kotlinx.coroutines.NonDisposableHandle.parent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class SignUpViewModel : ViewModel() {

	private val _toastMessage = MutableLiveData<String>()
	val toastMessage: LiveData<String> = _toastMessage

	private val _loading = MutableLiveData<Boolean>()
	val loading: LiveData<Boolean> = _loading

	private val _checkSuccess = MutableLiveData<Boolean>(false)
	val checkSuccess: LiveData<Boolean> = _checkSuccess

	private val _signupSuccess = MutableLiveData<Boolean>(false)
	val signupSuccess: LiveData<Boolean> = _signupSuccess


	private var checkCode = ""
	fun check(email: String) {

		_loading.postValue(true)
		postCheckAPI(email)
	}

	private fun postCheckAPI(email: String) {
		RetrofitClient.getApiService().email(CheckRequest(email)).enqueue(object : Callback<MyResponse<CheckData>> {
			override fun onResponse(call: Call<MyResponse<CheckData>>, response: Response<MyResponse<CheckData>>) {
				_loading.postValue(false)
				val code = response.code()
				if (code == 200) {
					val data = response.body()?.data ?: return
					checkCode = data.code
					_checkSuccess.postValue(true)
					_toastMessage.postValue(EMAIL_CODE_SEND_SUCCESS)
				}else{
					val body = response.errorBody()?.string()
					val data = GsonBuilder().create().fromJson(body, MyError::class.java)
					_toastMessage.postValue(data.error.toString())
				}
			}


			override fun onFailure(call: Call<MyResponse<CheckData>>, t: Throwable) {
				Log.e(TAG, "Error: ${t.message}")
				_loading.postValue(false)
			}
		})
	}

	fun signup(id: String, password: String, passwordCheck: String, email: String, nickname: String, code: String) {
		if (id.isEmpty()) {
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

		if (nickname.isEmpty()) {
			_toastMessage.postValue(NICKNAME_EMPTY_ERROR)
			return
		}

		if(code != checkCode){
			_toastMessage.postValue(EMAIL_CODE_SAME_ERROR)
			return
		}

		RetrofitClient.getApiService().signup(checkCode, SignUpRequest(id, password, nickname, email)).enqueue(object : Callback<MyResponse<String>> {
			override fun onResponse(call: Call<MyResponse<String>>, response: Response<MyResponse<String>>) {
				_loading.postValue(false)

				val code = response.code()
				val data = response.body()?.data

				if (code == 201) { // 회원가입 성공
					if (data == null) return

					MyApplication.prefUtil.setString("username", data)
					_toastMessage.postValue(SIGNUP_SUCCESS)
					_signupSuccess.postValue(true)
				}
				else if(code == 400){ // 회원가입 실패
					val body = response.errorBody()?.string()
					val data = GsonBuilder().create().fromJson(body, MyError::class.java)
					val error = data.error.toString()
					_toastMessage.postValue(error)
					if(error.equals(EMAIL_DUPLICATE_ERROR)){ // 이메일 중복
						_signupSuccess.postValue(false)
					}
				}else{
					Log.d(TAG, "$code  @${response.errorBody().toString()}")

				}
			}

			override fun onFailure(call: Call<MyResponse<String>>, t: Throwable) {
				Log.e(LoginViewModel.TAG, "Error: ${t.message}")
				_loading.postValue(false)
			}
		})
	}

	companion object {
		const val TAG = "SignUpViewModel"
	}
}
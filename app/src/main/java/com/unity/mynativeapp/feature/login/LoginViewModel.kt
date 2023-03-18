package com.unity.mynativeapp.feature.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.model.LoginData
import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.network.RetrofitClient
import com.unity.mynativeapp.util.ID_EMPTY_ERROR
import com.unity.mynativeapp.util.PW_EMPTY_ERROR
import com.unity.mynativeapp.util.X_ACCESS_TOKEN
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
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

	val JSON: MediaType = "application/json; charset=utf-8".toMediaType()

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
		val data = JSONObject()
		data.put("loginId", id)
		data.put("password", password)
		Log.d("check",id)
		Log.d("check",password)
		Log.d("check",data.toString())
		val logindata = data.toString().toRequestBody(JSON)
		postLoginAPI(logindata)
	}

	private fun postLoginAPI(logindata: RequestBody) {
		RetrofitClient.getApiService().login(logindata).enqueue(object : Callback<MyResponse<LoginData>> {
			override fun onResponse(call: Call<MyResponse<LoginData>>, response: Response<MyResponse<LoginData>>) {
				_loading.postValue(false)

				val data = response.body()?.data
				data?.let {
					if (data.accessToken.isEmpty()) {
						MyApplication.prefUtil.setString(X_ACCESS_TOKEN, data.accessToken)
					}
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
package com.unity.mynativeapp.Splash.Login.models

import com.unity.mynativeapp.Splash.Login.LoginActivity
import com.unity.mynativeapp.Splash.LoginResponse


interface LoginActivityInterface {
    fun  onPostLoginSuccess(response: LoginResponse)
    fun onPostLoginFailure(message: String)
}
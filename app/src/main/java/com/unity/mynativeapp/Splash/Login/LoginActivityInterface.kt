package com.unity.mynativeapp.Splash.Login


interface LoginActivityInterface {
    fun onPostLoginSuccess(response: LoginResponse)
    fun onPostLoginFailure(message: String)
}
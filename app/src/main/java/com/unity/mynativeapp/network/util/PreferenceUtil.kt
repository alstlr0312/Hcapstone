package com.unity.mynativeapp.network.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
	private val prefs: SharedPreferences = context.getSharedPreferences(SPFileName, MODE_PRIVATE)

	fun getString(key: String, defValue: String?): String? = prefs.getString(key, defValue)
	fun setString(key: String, value: String) = prefs.edit().putString(key, value).apply()
	fun removeString(key: String) = prefs.edit().remove(key).commit()

	fun getBoolean(key: String, defValue: Boolean): Boolean = prefs.getBoolean(key, defValue)
	fun setBoolean(key: String, value: Boolean) = prefs.edit().putBoolean(key, value).apply()

}
package com.unity.mynativeapp

import android.app.Application
import com.unity.mynativeapp.util.PreferenceUtil

class MyApplication: Application() {

    companion object {
        lateinit var prefUtil: PreferenceUtil
    }

    override fun onCreate() {
        prefUtil = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}
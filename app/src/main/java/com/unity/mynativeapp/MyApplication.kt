package com.unity.mynativeapp

import android.app.Application
import android.content.Intent
import android.util.Log
import com.unity.mynativeapp.util.PreferenceUtil
import java.lang.System.exit
import kotlin.system.exitProcess

class MyApplication: Application() {

    companion object {
        lateinit var prefUtil: PreferenceUtil
        var bodyPartHashMap = HashMap<String, String>()

    }

    override fun onCreate() {
        prefUtil = PreferenceUtil(applicationContext)

        bodyPartHashMap[getString(R.string.exercise_back)] = "BACK"
        bodyPartHashMap[getString(R.string.exercise_chest)] = "CHEST"
        bodyPartHashMap[getString(R.string.exercise_shoulder)] = "SHOULDER"
        bodyPartHashMap[getString(R.string.exercise_legs)] = "LEG"
        bodyPartHashMap[getString(R.string.exercise_abs)] = "ABS"
        bodyPartHashMap[getString(R.string.exercise_biceps)] = "BICEP"
        bodyPartHashMap[getString(R.string.exercise_triceps)] = "TRICEP"
        bodyPartHashMap[getString(R.string.exercise_cardio)] = "CARDIO"

        super.onCreate()
    }



}
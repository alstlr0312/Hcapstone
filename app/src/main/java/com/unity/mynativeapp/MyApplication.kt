package com.unity.mynativeapp

import android.app.Application
import com.unity.mynativeapp.network.util.PreferenceUtil


class MyApplication: Application() {

    companion object {
        lateinit var prefUtil: PreferenceUtil
        var bodyPartHashMap = HashMap<String, String>()
        var bodyPartToKoHashMap = HashMap<String, String>()
        var postTypeHashMap = HashMap<String, String>()
        var postTypeToKoHashMap = HashMap<String, String>()
        var workOutCategoryHashMap = HashMap<String, String>()
        var workOutCategoryToKoHashMap = HashMap<String, String>()
    }

    override fun onCreate() {
        prefUtil = PreferenceUtil(applicationContext)

        bodyPartHashMap[getString(R.string.exercise_back)] = "BACK"
        bodyPartHashMap[getString(R.string.exercise_chest)] = "CHEST"
        bodyPartHashMap[getString(R.string.exercise_shoulder)] = "SHOULDER"
        bodyPartHashMap[getString(R.string.exercise_legs)] = "LEGS"
        bodyPartHashMap[getString(R.string.exercise_abs)] = "ABS"
        bodyPartHashMap[getString(R.string.exercise_biceps)] = "BICEP"
        bodyPartHashMap[getString(R.string.exercise_triceps)] = "TRICEP"
        bodyPartHashMap[getString(R.string.exercise_cardio)] = "BACK"

        bodyPartToKoHashMap["BACK"] = getString(R.string.exercise_back)
        bodyPartToKoHashMap["CHEST"] = getString(R.string.exercise_chest)
        bodyPartToKoHashMap["SHOULDER"] = getString(R.string.exercise_shoulder)
        bodyPartToKoHashMap["LEGS"] = getString(R.string.exercise_legs)
        bodyPartToKoHashMap["ABS"] = getString(R.string.exercise_abs)
        bodyPartToKoHashMap["BICEP"] = getString(R.string.exercise_biceps)
        bodyPartToKoHashMap["TRICEP"] = getString(R.string.exercise_triceps)
        bodyPartToKoHashMap["CARDIO"] = getString(R.string.exercise_cardio)

        postTypeToKoHashMap["Q_AND_A"] = getString(R.string.q_and_a)
        postTypeToKoHashMap["KNOWLEDGE"] = getString(R.string.knowledge_sharing)
        postTypeToKoHashMap["SHOW_OFF"] = getString(R.string.show_off)
        postTypeToKoHashMap["COMPETITION"] = getString(R.string.assess)
        postTypeToKoHashMap["FREE"] = getString(R.string.free)

        postTypeHashMap[getString(R.string.q_and_a)] = "Q_AND_A"
        postTypeHashMap[getString(R.string.knowledge_sharing)] = "KNOWLEDGE"
        postTypeHashMap[getString(R.string.show_off)] = "SHOW_OFF"
        postTypeHashMap[getString(R.string.assess)] = "COMPETITION"
        postTypeHashMap[getString(R.string.free)] = "FREE"


        workOutCategoryToKoHashMap["HEALTH"] = getString(R.string.health)
        workOutCategoryToKoHashMap["PILATES"] = getString(R.string.pilates)
        workOutCategoryToKoHashMap["YOGA"] = getString(R.string.yoga)
        workOutCategoryToKoHashMap["JOGGING"] = getString(R.string.jogging)
        workOutCategoryToKoHashMap["ETC"] = getString(R.string.etc)

        workOutCategoryHashMap[getString(R.string.health)] = "HEALTH"
        workOutCategoryHashMap[getString(R.string.pilates)] = "PILATES"
        workOutCategoryHashMap[getString(R.string.yoga)] = "YOGA"
        workOutCategoryHashMap[getString(R.string.jogging)] = "JOGGING"
        workOutCategoryHashMap[getString(R.string.etc)] = "ETC"

        super.onCreate()
    }



}
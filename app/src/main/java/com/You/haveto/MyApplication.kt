package com.You.haveto

import android.app.Application
import com.You.haveto.network.util.PreferenceUtil


class MyApplication: Application() {

    companion object {
        lateinit var prefUtil: PreferenceUtil
        var bodyPartHashMap = HashMap<String, String>()
        var bodyPartKorHashMap = HashMap<String, String>()
        var postCategoryHashMap = HashMap<String, String>()
        var postCategoryKorHashMap = HashMap<String, String>()
        var postExerciseTypeHashMap = HashMap<String, String>()
        var postExerciseTypeKorHashMap = HashMap<String, String>()
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
        bodyPartHashMap[getString(R.string.exercise_cardio)] = "BACK"

        bodyPartKorHashMap["BACK"] = getString(R.string.exercise_back)
        bodyPartKorHashMap["CHEST"] = getString(R.string.exercise_chest)
        bodyPartKorHashMap["SHOULDER"] = getString(R.string.exercise_shoulder)
        bodyPartKorHashMap["LEG"] = getString(R.string.exercise_legs)
        bodyPartKorHashMap["ABS"] = getString(R.string.exercise_abs)
        bodyPartKorHashMap["BICEP"] = getString(R.string.exercise_biceps)
        bodyPartKorHashMap["TRICEP"] = getString(R.string.exercise_triceps)
        bodyPartKorHashMap["CARDIO"] = getString(R.string.exercise_cardio)

        postCategoryKorHashMap["Q_AND_A"] = getString(R.string.q_and_a)
        postCategoryKorHashMap["KNOWLEDGE"] = getString(R.string.knowledge_sharing)
        postCategoryKorHashMap["SHOW_OFF"] = getString(R.string.show_off)
        postCategoryKorHashMap["COMPETITION"] = getString(R.string.assess)
        postCategoryKorHashMap["FREE"] = getString(R.string.free)

        postCategoryHashMap[getString(R.string.q_and_a)] = "Q_AND_A"
        postCategoryHashMap[getString(R.string.knowledge_sharing)] = "KNOWLEDGE"
        postCategoryHashMap[getString(R.string.show_off)] = "SHOW_OFF"
        postCategoryHashMap[getString(R.string.assess)] = "COMPETITION"
        postCategoryHashMap[getString(R.string.free)] = "FREE"


        postExerciseTypeKorHashMap["HEALTH"] = getString(R.string.health)
        postExerciseTypeKorHashMap["PILATES"] = getString(R.string.pilates)
        postExerciseTypeKorHashMap["YOGA"] = getString(R.string.yoga)
        postExerciseTypeKorHashMap["JOGGING"] = getString(R.string.jogging)
        postExerciseTypeKorHashMap["ETC"] = getString(R.string.etc)

        postExerciseTypeHashMap[getString(R.string.health)] = "HEALTH"
        postExerciseTypeHashMap[getString(R.string.pilates)] = "PILATES"
        postExerciseTypeHashMap[getString(R.string.yoga)] = "YOGA"
        postExerciseTypeHashMap[getString(R.string.jogging)] = "JOGGING"
        postExerciseTypeHashMap[getString(R.string.etc)] = "ETC"

        super.onCreate()
    }



}
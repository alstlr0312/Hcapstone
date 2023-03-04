package com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv.AddExercise.AddexModels

import  retrofit2.Call
import retrofit2.http.*


interface AddexResponse {

    @FormUrlEncoded
    @POST("diary/write")
    fun AddexResponse(
        @Header("AUTHORIZATION") AUTH: String,
        @Field("writeDiaryDto") writeDiaryDto: String
    ): Call<ExData>

}

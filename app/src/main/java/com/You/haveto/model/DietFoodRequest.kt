package com.You.haveto.model

import com.google.gson.annotations.SerializedName

data class DietFoodRequest(
    @SerializedName("food") val food: ArrayList<String>,
    @SerializedName("healthPurpose") val healthPurpose: String,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
)

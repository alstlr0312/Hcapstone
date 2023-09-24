package com.You.haveto.model

import com.google.gson.annotations.SerializedName

data class RoutineRequest(
    @SerializedName("healthPurpose") val healthPurpose: String,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
    @SerializedName("divisions") val divisions: Int,
)

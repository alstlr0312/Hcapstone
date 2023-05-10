package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName

data class MapResponse(
    @SerializedName(" LOCALDATA_103701_JG") val exerciseInfo: List<jgitem>,
)
data class jgitem(
    @SerializedName("isFirst") val isFirst: Boolean? = null,
    @SerializedName("list_total_count") val list_total_count: Int? = null,
    @SerializedName("isLast") val isLast: Boolean?= null,
    @SerializedName("totalPages") val totalPages : Int?= null,
    @SerializedName("row") val row: List<rowItem>,
    @SerializedName("bodyPart") val bodyPart: String,
    @SerializedName("finished") val finished: Boolean = false,
)

data class rowItem(
    @SerializedName("SITEWHLADDR") val SITEWHLADDR: String? = null,
    @SerializedName("UPDATEGBN") val UPDATEGBN: String? = null,
    @SerializedName("RDNWHLADDR") val RDNWHLADDR: String? = null,
    @SerializedName("LASTMODTS") val LASTMODTS: String? = null,
    @SerializedName("TRDSTATENM") val TRDSTATENM: String? = null,
    @SerializedName("BPLCNM") val BPLCNM: String? = null,
    @SerializedName("DTLSTATENM") val DTLSTATENM: String? = null,
)
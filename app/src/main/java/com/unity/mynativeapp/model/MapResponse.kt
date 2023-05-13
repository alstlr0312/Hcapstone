package com.unity.mynativeapp.model

import com.google.gson.annotations.SerializedName

data class MapResponse(
    @SerializedName("LOCALDATA_103701_JG") val LOCALDATA_103701_JG: LocalData? = null,
    @SerializedName("LOCALDATA_103701_JN") val LOCALDATA_103701_JN: LocalData? = null,
    @SerializedName("LOCALDATA_103701_DB") val LOCALDATA_103701_DB: LocalData? = null,
    @SerializedName("LOCALDATA_103701_SP") val LOCALDATA_103701_SP: LocalData? = null,
    @SerializedName("LOCALDATA_103701_GR") val LOCALDATA_103701_GR: LocalData? = null,
    @SerializedName("LOCALDATA_103701_JR") val LOCALDATA_103701_JR: LocalData? = null,
    @SerializedName("LOCALDATA_103701_SD") val LOCALDATA_103701_SD: LocalData? = null,
    @SerializedName("LOCALDATA_103701_GD") val LOCALDATA_103701_GD: LocalData? = null,
    @SerializedName("LOCALDATA_103701_DD") val LOCALDATA_103701_DD: LocalData? = null,
    @SerializedName("LOCALDATA_103701_YS") val LOCALDATA_103701_YS: LocalData? = null,
    @SerializedName("LOCALDATA_103701_GN") val LOCALDATA_103701_GN: LocalData? = null,
    @SerializedName("LOCALDATA_103701_GS") val LOCALDATA_103701_GS: LocalData? = null,
    @SerializedName("LOCALDATA_103701_YD") val LOCALDATA_103701_YD: LocalData? = null,
    @SerializedName("LOCALDATA_103701_NW") val LOCALDATA_103701_NW: LocalData? = null,
    @SerializedName("LOCALDATA_103701_DJ") val LOCALDATA_103701_DJ: LocalData? = null,
    @SerializedName("LOCALDATA_103701_EP") val LOCALDATA_103701_EP: LocalData? = null,
    @SerializedName("LOCALDATA_103701_GA") val LOCALDATA_103701_GA: LocalData? = null,
    @SerializedName("LOCALDATA_103701_YC") val LOCALDATA_103701_YC: LocalData? = null,
    @SerializedName("LOCALDATA_103701_SB") val LOCALDATA_103701_SB: LocalData? = null,
    @SerializedName("LOCALDATA_103701_MP") val LOCALDATA_103701_MP: LocalData? = null,
    @SerializedName("LOCALDATA_103701_SM") val LOCALDATA_103701_SM: LocalData? = null,
    @SerializedName("LOCALDATA_103701_GC") val LOCALDATA_103701_GC: LocalData? = null,
    @SerializedName("LOCALDATA_103701_GJ") val LOCALDATA_103701_GJ: LocalData? = null,
    @SerializedName("LOCALDATA_103701_SC") val LOCALDATA_103701_SC: LocalData? = null,
    @SerializedName("LOCALDATA_103701_GB") val LOCALDATA_103701_GB: LocalData? = null,

)
data class LocalData(
    @SerializedName("isFirst") val isFirst: Boolean? = null,
    @SerializedName("list_total_count") val list_total_count: Int? = null,
    @SerializedName("isLast") val isLast: Boolean?= null,
    @SerializedName("totalPages") val totalPages : Int?= null,
    @SerializedName("row") val row: List<rowItem>
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

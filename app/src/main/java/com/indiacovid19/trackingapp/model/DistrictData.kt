package com.indiacovid19.trackingapp.model

data class DistrictData(
    val confirmed: Int,
    val delta: DeltaX,
    val district: String,
    val lastupdatedtime: String
) : Comparable<DistrictData> {
    override fun compareTo(other: DistrictData): Int {
        return this.confirmed.compareTo(other.confirmed)
    }

}
package com.indiacovid19.trackingapp.model

data class StatewiseResponseItem(
    val districtData: List<DistrictData>,
    val state: String
)
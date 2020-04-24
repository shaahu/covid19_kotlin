package com.indiacovid19.trackingapp.model

data class MainResponse(
    val cases_time_series: List<CasesTimeSery>,
    val key_values: List<KeyValue>,
    val statewise: List<Statewise>,
    val tested: List<Tested>
) {
    constructor() : this(
        emptyList(), emptyList(),
        emptyList(), emptyList()
    )
}
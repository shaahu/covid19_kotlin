package com.indiacovid19.trackingapp.model

data class CasesTimeSery(
    val dailyconfirmed: String,
    val dailydeceased: String,
    val dailyrecovered: String,
    val date: String,
    val totalconfirmed: String,
    val totaldeceased: String,
    val totalrecovered: String
)
package com.indiacovid19.trackingapp.model

data class Tested(
    val source: String,
    val testsconductedbyprivatelabs: String,
    val totalindividualstested: String,
    val totalpositivecases: String,
    val totalsamplestested: String,
    val updatetimestamp: String
)
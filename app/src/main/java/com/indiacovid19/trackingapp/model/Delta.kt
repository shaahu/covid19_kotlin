package com.indiacovid19.trackingapp.model

data class Delta(
    val active: Int,
    val confirmed: Int,
    val deaths: Int,
    val recovered: Int
){
    constructor():this(0,0,0,0)
}
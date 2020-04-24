package com.indiacovid19.trackingapp.model

data class Statewise(
    val active: String,
    val confirmed: String,
    val deaths: String,
    val delta: Delta,
    val deltaconfirmed: String,
    val deltadeaths: String,
    val deltarecovered: String,
    val lastupdatedtime: String,
    val recovered: String,
    val state: String,
    val statecode: String
) {
    constructor() : this(
        "", "", "", Delta(), "", "", "", "", "", "", ""
    )
}
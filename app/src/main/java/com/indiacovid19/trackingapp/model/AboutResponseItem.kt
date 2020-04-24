package com.indiacovid19.trackingapp.model

data class AboutResponseItem(
    val ans: String,
    val place: Int,
    val que: String
) : Comparable<AboutResponseItem> {
    override fun compareTo(other: AboutResponseItem): Int {
        return this.place.compareTo(other.place)
    }

}
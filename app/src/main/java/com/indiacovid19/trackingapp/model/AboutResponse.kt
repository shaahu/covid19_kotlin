package com.indiacovid19.trackingapp.model

class AboutResponse() : ArrayList<AboutResponseItem>(), Comparable<AboutResponseItem> {


    override fun compareTo(other: AboutResponseItem): Int {
        return this.compareTo(other)
    }
}
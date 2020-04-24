package com.indiacovid19.trackingapp.helper

/**
 * Created by Shahu Ronghe on 04, April, 2020
 * in Covid-19 India Stats App
 */
interface ExpandableProperty {
    fun getResourceId(): Int

    fun isExpand(): Boolean

    fun setExpand(expand: Boolean)

    fun hasChild(): Boolean

    fun setHasChild(hasChild: Boolean)

    fun doNotCollapse()

    fun isDoNotCollapse(): Boolean
}
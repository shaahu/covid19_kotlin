package com.indiacovid19.trackingapp.helper

import android.view.View


/**
 * Created by Shahu Ronghe on 04, April, 2020
 * in Covid-19 India Stats App
 */
interface HolderProperty {
    fun bindIds(itemView: View?)
    fun setData(data: ExpandableAttribute?)
    fun onCollapse(data: ExpandableAttribute?)
    fun onExpand(data: ExpandableAttribute?)
}
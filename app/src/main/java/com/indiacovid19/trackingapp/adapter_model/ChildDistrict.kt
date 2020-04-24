package com.indiacovid19.trackingapp.adapter_model

import com.indiacovid19.trackingapp.helper.ExpandableAttribute

/**
 * Created by Shahu Ronghe on 04, April, 2020
 * in Covid-19 India Stats App
 */
class ChildDistrict(
    resourceId: Int,
    val districtName: String,
    val districtConfirmedCount: Int,
    val delta: Int,
    val count: Int
) : ExpandableAttribute(resourceId)
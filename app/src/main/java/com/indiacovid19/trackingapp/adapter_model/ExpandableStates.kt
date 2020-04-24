package com.indiacovid19.trackingapp.adapter_model

import com.indiacovid19.trackingapp.helper.ExpandableAttribute

/**
 * Created by Shahu Ronghe on 04, April, 2020
 * in Covid-19 India Stats App
 */
open class ExpandableStates(
    resourceId: Int,
    stateName: String?,
    confirmedCount: Int,
    deltaConfirmed: Int,
    activeCount: Int,
    recoveredCount: Int,
    deltaRecovered: Int,
    deceasedCount: Int,
    deltaDeceased: Int,
    val stateCount: Int
) : ExpandableAttribute(resourceId) {
    var stateName: String? = stateName
        private set
    var confirmedCount = confirmedCount
        private set
    var activeCount = activeCount
        private set
    var recoveredCount = recoveredCount
        private set
    var deceasedCount = deceasedCount
        private set
    var deltaConfirmed = deltaConfirmed
        private set
    var deltaRecovered = deltaRecovered
        private set
    var deltaDeceased = deltaDeceased
        private set

}
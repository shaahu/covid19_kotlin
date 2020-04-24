package com.indiacovid19.trackingapp.helper

import com.indiacovid19.trackingapp.model.DistrictData
import com.indiacovid19.trackingapp.model.StatewiseResponse

/**
 * Created by Shahu Ronghe on 05, April, 2020
 * in Covid-19 India Stats App
 */
object Extractor {
    fun getDistrictData(data: StatewiseResponse, name: String): List<DistrictData> {
        data.forEach {
            if (it.state == name)
                return it.districtData
        }
        return emptyList()
    }

    fun getPercentage(total: String, of: String): String {
        return "%.0f".format(of.toFloat() / total.toFloat() * 100) + "%"
    }
}
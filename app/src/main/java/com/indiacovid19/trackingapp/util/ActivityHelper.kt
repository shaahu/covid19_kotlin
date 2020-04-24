package com.indiacovid19.trackingapp.util

import androidx.appcompat.app.AppCompatDelegate
import com.indiacovid19.trackingapp.helper.AppSharedPreferenceHelper

/**
 * Created by Shahu Ronghe on 12, April, 2020
 * in Covid-19 India Stats App
 */
object ActivityHelper {

    fun setMode() {
        if (AppSharedPreferenceHelper.isLightModeOn()!!) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    fun loadStateDropDownList(): Array<String> {
        return arrayOf(
            "<Select your state>",
            "Andhra Pradesh",
            "Arunachal Pradesh",
            "Assam",
            "Bihar",
            "Chhattisgarh",
            "Goa",
            "Gujarat",
            "Haryana",
            "Himachal Pradesh",
            "Jharkhand",
            "Karnataka",
            "Kerala",
            "Madhya Pradesh",
            "Maharashtra",
            "Manipur",
            "Meghalaya",
            "Mizoram",
            "Nagaland",
            "Odisha",
            "Punjab",
            "Rajasthan",
            "Sikkim",
            "Tamil Nadu",
            "Telangana",
            "Tripura",
            "Uttarakhand",
            "Uttar Pradesh",
            "West Bengal",
            "Andaman and Nicobar Islands",
            "Chandigarh",
            "Dadra and Nagar Haveli",
            "Daman and Diu",
            "Delhi",
            "Jammu and Kashmir",
            "Ladakh",
            "Lakshadweep",
            "Puducherry"
        )
    }

    fun loadRefreshInterval(): Array<String> {
        return arrayOf(
            "5 min",
            "15 min",
            "30 min",
            "1 hour",
            "2 hours",
            "4 hours",
            "8 hours",
            "12 hours",
            "24 hours"
        )
    }

    fun refreshIntervalMapping(value: String): Long {
        when (value) {
            "5 min" -> return 5 * 60
            "15 min" -> return 15 * 60
            "30 min" -> return 30 * 60
            "1 hour" -> return 1 * 60
            "2 hours" -> return 2 * 60
            "4 hours" -> return 4 * 60
            "8 hours" -> return 8 * 60
            "12 hours" -> return 12 * 60
            "24 hours" -> return 24 * 60
        }
        return 0
    }
}
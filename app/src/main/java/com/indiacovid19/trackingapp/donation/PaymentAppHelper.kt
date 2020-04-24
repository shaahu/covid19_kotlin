package com.indiacovid19.trackingapp.donation

import android.content.Context
import android.content.pm.PackageManager


/**
 * Created by Shahu Ronghe on 08, April, 2020
 * in Covid-19 India Stats App
 */

private fun checkAppExist(packageName: String, context:Context): Boolean {
    val pm: PackageManager = context.packageManager
    try {
        pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        return true
    } catch (e: PackageManager.NameNotFoundException) {
    }
    return false
}
package com.indiacovid19.trackingapp.helper

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.google.gson.GsonBuilder
import com.indiacovid19.trackingapp.helper.Constant.IS_LIGHT_MODE_ON
import com.indiacovid19.trackingapp.helper.Constant.MAIN_RESPONSE
import com.indiacovid19.trackingapp.model.MainResponse


/**
 * Created by Shahu Ronghe on 08, April, 2020
 * in Covid-19 India Stats App
 */
object AppSharedPreferenceHelper {
    private var instance: AppSharedPreferenceHelper? = null

    private var context: Context? = null
    private var sharedPref: SharedPreferences? = null
    private var sharedPrefEditor: Editor? = null

    fun instance(context: Context?): AppSharedPreferenceHelper? {
        if (instance == null) {
            instance = AppSharedPreferenceHelper
            if (context != null) {
                configSessionUtils(
                    context
                )
            }
        }
        return instance
    }

    fun instance(): AppSharedPreferenceHelper? {
        return instance
    }

    private fun configSessionUtils(context: Context) {
        AppSharedPreferenceHelper.context = context
        sharedPref =
            context?.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE)
        sharedPrefEditor = sharedPref?.edit()
    }

    fun putValue(key: String?, value: String?) {
        if (value != null) {
            sharedPrefEditor!!.putString(key, value)
        }
        sharedPrefEditor!!.commit()
    }

    fun putValue(key: String?, value: Boolean?) {
        if (value != null) {
            sharedPrefEditor!!.putBoolean(key, value)
        }
        sharedPrefEditor!!.commit()
    }

    fun getValueAsString(key: String?): String? {
        return sharedPref!!.getString(key, null)
    }

    fun getValueAsBoolean(key: String?, def: Boolean? = null): Boolean? {
        if (def != null) {
            return sharedPref!!.getBoolean(key, def)
        }
        return sharedPref!!.getBoolean(key, false)
    }

    fun isLightModeOn(): Boolean? {
        return sharedPref!!.getBoolean(IS_LIGHT_MODE_ON, false)
    }

    fun setLightModeOn(value: Boolean) {
        sharedPrefEditor!!.putBoolean(IS_LIGHT_MODE_ON, value)
        sharedPrefEditor!!.commit()
    }

    fun storeMainResponse(key: String, mainResponse: MainResponse) {
        val gsonBuilder = GsonBuilder()
        val gson = gsonBuilder.create()
        val data = gson.toJson(mainResponse)
        sharedPrefEditor!!.putString(key, data).commit()
    }

    fun getStoredMainResponse(): MainResponse? {
        val data: String? = sharedPref?.getString(MAIN_RESPONSE, null)
        if (data != null) {
            val gsonBuilder = GsonBuilder()
            val gson = gsonBuilder.create()
            return gson.fromJson(data, MainResponse::class.java)
        }
        return null
    }
}
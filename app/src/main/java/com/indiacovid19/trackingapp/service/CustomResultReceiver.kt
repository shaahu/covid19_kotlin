package com.indiacovid19.trackingapp.service

import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import com.indiacovid19.trackingapp.MainActivity


/**
 * Created by Shahu Ronghe on 13, April, 2020
 * in Covid-19 India Stats App
 */
class CustomResultReceiver(
    handler: Handler?,
    receiver: MainActivity
) :
    ResultReceiver(handler) {
    /*
     * Step 1: The AppReceiver is just a custom interface class we created.
     * This interface is implemented by the activity
     */
    private val appReceiver: MainActivity = receiver
    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
        appReceiver.onReceiveResult(resultCode, resultData)
    }

}
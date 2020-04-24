package com.indiacovid19.trackingapp.service

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import com.indiacovid19.trackingapp.helper.AppSharedPreferenceHelper
import com.indiacovid19.trackingapp.helper.Constant.REFRESH_INTERVAL
import com.indiacovid19.trackingapp.util.ActivityHelper
import java.util.*
import kotlin.concurrent.timerTask

/**
 * Created by Shahu Ronghe on 12, April, 2020
 * in Covid-19 India Stats App
 */
class Covid19RefreshService : IntentService(Covid19RefreshService::class.java.name) {
    override fun onHandleIntent(p0: Intent?) {
        val delay = AppSharedPreferenceHelper.getValueAsString(REFRESH_INTERVAL)?.let {
            ActivityHelper.refreshIntervalMapping(
                it
            )
        }
        val t = Timer()
        if (delay != null) {
            t.scheduleAtFixedRate(
                timerTask {
                    val b = Bundle()

                    p0?.getParcelableExtra<ResultReceiver?>("receiver")?.send(STATUS_FINISHED, b)
                },
                1000,
                delay * 1000
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        const val STATUS_RUNNING = 0
        const val STATUS_FINISHED = 1
        const val STATUS_ERROR = 2
    }
}

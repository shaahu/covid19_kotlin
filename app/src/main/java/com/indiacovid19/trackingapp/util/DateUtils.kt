package com.indiacovid19.trackingapp.util

import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Shahu Ronghe on 03, April, 2020
 * in Covid-19 India Stats App
 */
object DateUtils {

    fun toSimpleString(date: String): String {
        val parser = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val formatter = SimpleDateFormat("dd MMM, HH:mm")
        return formatter.format(parser.parse(date))
    }

    fun stringToDate(date: String): Date {
        val parser = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        return parser.parse(date)
    }


    fun calculateAgoTime(calendar: Calendar, date2: Date): String? {
        var returnString: String? = null
        val diff: Long = calendar.time.time - date2.time
        val seconds = diff / 1000
        val min = seconds / 60
        val hour = min / 60
        val days = hour / 24
        if (min < 60)
            returnString = "$min MINUTES AGO"
        if (min > 60) {
            returnString = if (hour > 1)
                "ABOUT $hour HOURS AGO"
            else
                "ABOUT $hour HOUR AGO"
        }
        if (hour > 24) {
            returnString = if (days > 1)
                "ABOUT $days DAY AGO"
            else
                "ABOUT $days DAYS AGO"
        }
        return returnString
    }

    fun getMinutesDifference(timeStart: Long, timeStop: Long): Long {
        val diff = timeStop - timeStart
        return diff / (60 * 1000)
    }

    fun getCurrentTime(): String {
        val cal = Calendar.getInstance().time
        val sdf =
            SimpleDateFormat("hh:mm aa", Locale.getDefault())
        return sdf.format(cal)
    }

    fun isNight(): Boolean {
        val isNight: Boolean
        val cal = Calendar.getInstance()
        val hour = cal[Calendar.HOUR_OF_DAY]
        isNight = hour < 6 || hour > 18
        return isNight
    }
}
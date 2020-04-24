package com.indiacovid19.trackingapp.helper

/**
 * Created by Shahu Ronghe on 04, April, 2020
 * in Covid-19 India Stats App
 */
object PlaceHolder {
    fun getDeltaString(data: String): String {
        return "(+$data)"
    }

    fun getDateString(dateString: String, ago: String?): CharSequence? {
        return "$dateString IST ($ago)"
    }

    fun getCountryTotal(newConfirmTotal: Int, newRecoveredTotal: Int, newDeathTotal: Int): String {
        val stringBuilder = StringBuilder()
        if (newConfirmTotal > 0)
            stringBuilder.append("New Confirm: $newConfirmTotal").append("\n")
        if (newRecoveredTotal > 0)
            stringBuilder.append("New Recovered: $newRecoveredTotal").append("\n")
        if (newDeathTotal > 0)
            stringBuilder.append("New Death: $newDeathTotal")
        return stringBuilder.toString()
    }

    fun getStateTotal(newConfirmState: Int, newRecoveredState: Int, newDeathState: Int): String {
        val stringBuilder: StringBuilder = StringBuilder()
        if (newConfirmState > 0)
            stringBuilder.append("New Confirm: $newConfirmState").append("\n")
        if (newRecoveredState > 0)
            stringBuilder.append("New Recovered: $newRecoveredState").append("\n")
        if (newDeathState > 0)
            stringBuilder.append("New Death: $newDeathState")
        return stringBuilder.toString()
    }

}
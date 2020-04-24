package com.indiacovid19.trackingapp.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import java.io.File

/**
 * Created by Shahu Ronghe on 09, April, 2020
 * in Covid-19 India Stats App
 */
object DownloadUtils {
    fun checkFiles(dir: String): Int {
        val directory = File(dir)
        val dirs: Array<File> = directory.listFiles() ?: return 0
        return dirs.size
    }

    fun getDownloadPath(context: Context): String? {
        val m: PackageManager = context.packageManager
        val s = context.packageName
        val p: PackageInfo = m.getPackageInfo(s, 0)
        return p.applicationInfo.dataDir
    }

    fun getFilesInDirectory(dir: String): Array<File> {
        val directory = File(dir)
        return directory.listFiles() ?: return emptyArray()
    }
}
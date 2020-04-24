package com.indiacovid19.trackingapp.util

import android.util.Log
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

/**
 * Created by Shahu Ronghe on 08, April, 2020
 * in Covid-19 India Stats App
 */
class Unzipper(zipFile: File?, path: String?) {
    private var _zipFile: File? = zipFile
    private var _zipFileStream: InputStream? = null
    private var ROOT_LOCATION: String? = path
    private val TAG = "Unzipper"

    init {
        dirChecker("")
    }

    fun unzip() {
        try {
            Log.i(TAG, "Starting to unzip")
            var fin: InputStream? = _zipFileStream
            if (fin == null) {
                fin = FileInputStream(_zipFile)
            }
            val zin = ZipInputStream(fin)
            var ze: ZipEntry?
            while (zin.nextEntry.also { ze = it } != null) {
                if (ze?.isDirectory!!) {
                    dirChecker(ROOT_LOCATION + "/" + ze!!.name)
                } else {
                    val fout = FileOutputStream(File(ROOT_LOCATION, ze!!.name))
                    val baos = ByteArrayOutputStream()
                    val buffer = ByteArray(1024)
                    var count: Int

                    while (zin.read(buffer).also { count = it } != -1) {
                        baos.write(buffer, 0, count)
                        val bytes: ByteArray = baos.toByteArray()
                        fout.write(bytes)
                        baos.reset()
                    }
                    fout.close()
                    zin.closeEntry()
                }
            }
            zin.close()
            Log.i(TAG, "Finished unzip")
        } catch (e: Exception) {
            Log.e(TAG, "Unzip Error", e)
        }
    }

    private fun dirChecker(dir: String) {
        val f = File(dir)
        Log.i(TAG, "creating dir $dir")
        if (dir.length >= 0 && !f.isDirectory) {
            f.mkdirs()
        }
    }
}
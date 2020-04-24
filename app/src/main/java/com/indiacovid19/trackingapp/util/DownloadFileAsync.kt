package com.indiacovid19.trackingapp.util

import android.os.AsyncTask
import android.util.Log
import java.io.*
import java.net.URL
import java.net.URLConnection


/**
 * Created by Shahu Ronghe on 08, April, 2020
 * in Covid-19 India Stats App
 */
class DownloadFileAsync(
    private val downloadLocation: String,
    private val callback: PostDownload?
) :
    AsyncTask<String?, String?, String?>() {
    private var fd: FileDescriptor? = null
    private var file: File? = null

    override fun doInBackground(vararg aurl: String?): String? {
        var count: Int
        try {
            val url = URL(aurl[0])
            val connection: URLConnection = url.openConnection()
            connection.connect()
            val lengthOfFile: Int = connection.contentLength
            Log.d(TAG, "Length of the file: $lengthOfFile")
            val input: InputStream = BufferedInputStream(url.openStream())
            file = File(downloadLocation)
            val output = FileOutputStream(file)
            Log.d(TAG, "file saved at " + file!!.absolutePath)
            fd = output.fd
            val data = ByteArray(1024)
            var total: Long = 0
            while (input.read(data).also { count = it } != -1) {
                total += count.toLong()
                publishProgress("" + (total * 100 / lengthOfFile).toInt())
                output.write(data, 0, count)
            }
            output.flush()
            output.close()
            input.close()
        } catch (e: Exception) {
            Log.e(TAG, "GENERAL EXCEPTION: ", e)
        }
        return null
    }

    override fun onProgressUpdate(vararg progress: String?) {
        Log.d("progress", "progress: " + progress.contentToString())
        callback?.downloadProgress(progress.first())
    }

    override fun onPostExecute(unused: String?) {
        callback?.downloadDone(file)
    }

    interface PostDownload {
        fun downloadDone(fd: File?)
        fun downloadProgress(value: String?)
    }

    companion object {
        private const val TAG = "DownloadFileAsync"
    }

}
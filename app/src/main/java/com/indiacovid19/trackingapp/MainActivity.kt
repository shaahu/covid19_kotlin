package com.indiacovid19.trackingapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.indiacovid19.trackingapp.adapter.ExpandableRecyclerViewAdapter
import com.indiacovid19.trackingapp.adapter_model.ChildDistrict
import com.indiacovid19.trackingapp.adapter_model.ExpandableStates
import com.indiacovid19.trackingapp.helper.*
import com.indiacovid19.trackingapp.helper.Constant.DEVICE_STATE_REGISTER
import com.indiacovid19.trackingapp.helper.Constant.DISCLAIMER_STATUS
import com.indiacovid19.trackingapp.helper.Constant.DOWNLOAD_DIR
import com.indiacovid19.trackingapp.helper.Constant.GRAPHICS_ASSET_LINK
import com.indiacovid19.trackingapp.helper.Constant.GRAPHICS_ASSET_PATH
import com.indiacovid19.trackingapp.helper.Constant.GRAPHICS_ASSET_ZIP
import com.indiacovid19.trackingapp.helper.Constant.GRAPHICS_DOWNLOAD_STATUS
import com.indiacovid19.trackingapp.helper.Constant.MAIN_RESPONSE
import com.indiacovid19.trackingapp.helper.Constant.NOTIFICATION_ENABLED
import com.indiacovid19.trackingapp.helper.Constant.PERMISSION_STATUS
import com.indiacovid19.trackingapp.helper.Constant.REFRESH_INTERVAL
import com.indiacovid19.trackingapp.helper.Constant.STATE_WISE_DATA_URL
import com.indiacovid19.trackingapp.helper.Constant.USER_DEVICE_REGISTERED
import com.indiacovid19.trackingapp.helper.Constant.USER_STATE_NAME
import com.indiacovid19.trackingapp.holder.DistrictViewHolder
import com.indiacovid19.trackingapp.holder.ExpandableViewHolder
import com.indiacovid19.trackingapp.holder.StateViewHolder
import com.indiacovid19.trackingapp.model.DistrictData
import com.indiacovid19.trackingapp.model.MainResponse
import com.indiacovid19.trackingapp.model.Statewise
import com.indiacovid19.trackingapp.model.StatewiseResponse
import com.indiacovid19.trackingapp.service.Covid19RefreshService
import com.indiacovid19.trackingapp.service.CustomResultReceiver
import com.indiacovid19.trackingapp.util.*
import com.indiacovid19.trackingapp.util.ActivityHelper.loadRefreshInterval
import com.indiacovid19.trackingapp.util.ActivityHelper.loadStateDropDownList
import com.indiacovid19.trackingapp.util.DateUtils.isNight
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_disclaimer_dialog.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Shahu Ronghe on 03, April, 2020
 * in Covid-19 India Stats App
 */
class MainActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {
    companion object {
        private const val TAG = "MainActivity"
    }

    private var notificationManager: NotificationManager? = null
    private lateinit var mRequestQueue: RequestQueue
    private var date = Date()
    private var mainResponse = MainResponse()
    private var statewiseResponse = StatewiseResponse()
    private var mStatewiseModified = emptyList<Statewise>()
    private var sortMap: HashMap<String, Boolean> = HashMap()
    private lateinit var adapter: ExpandableRecyclerViewAdapter
    private var mInternetAvailable = true
    private val numOfDownloadedDirectory = 1
    private val showDisclaimer = true
    private var resultReceiver: CustomResultReceiver? = null
    private var mActivityMinimized = false
    private val connectivityReceiver = ConnectivityReceiver()
    private var downloadDir: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        AppSharedPreferenceHelper.instance(this)
        ActivityHelper.setMode()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mRequestQueue = Volley.newRequestQueue(this)
        Log.e(TAG, "Skillet")
        if (!AppSharedPreferenceHelper.getValueAsBoolean(DISCLAIMER_STATUS)!! && showDisclaimer) {
            showDisclaimer()
        } else {
            startLoadingData()
        }
        initializeListeners()
    }

    private fun startLoadingData() {
        loadStateData()
        downloadManagement()
        notificationManagement()
    }

    private fun notificationManagement() {
        if (AppSharedPreferenceHelper.getValueAsBoolean(NOTIFICATION_ENABLED, true)!!) {
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager!!.cancelAll()
            invokeBackgroundService()
        } else {
            Log.d("notifications: ", "notification disabled!")
        }
    }

    private fun downloadManagement() {
        if (AppSharedPreferenceHelper.getValueAsBoolean(USER_DEVICE_REGISTERED)!!) {
            //register again
            if (AppSharedPreferenceHelper.getValueAsString(USER_STATE_NAME) != null)
                registerDevice(AppSharedPreferenceHelper.getValueAsString(USER_STATE_NAME))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionsUtil.checkPermissions(this)) {
                downloadInitialization()
            } else {
                PermissionsUtil.askPermissions(this, this)
            }
        } else {
            downloadInitialization()
        }
    }

    private fun initializeListeners() {
        pull_to_refresh.setOnRefreshListener {
            loadStateData()
        }
        info_btn.setOnClickListener {
            info_btn.isEnabled = false
            val infoIntent = Intent(this, InfoActivity::class.java)
            infoIntent.putExtra(DOWNLOAD_DIR, downloadDir)
            startActivity(infoIntent)
        }
        settings_btn.setOnClickListener {
            settings_btn.isEnabled = false
            val infoIntent = Intent(this, Settings::class.java)
            startActivity(infoIntent)
        }
    }

    private fun downloadInitialization() {
        downloadDir = DownloadUtils.getDownloadPath(applicationContext)
        val graphicsCheckPath = downloadDir + GRAPHICS_ASSET_PATH
        if (graphicsCheckPath.let { DownloadUtils.checkFiles(it) } != numOfDownloadedDirectory) {
            Log.d(TAG, "downloadInitialization: directory count mismatch, starting download")
            downloadAndUnzipContent()
        } else {
            Log.d(TAG, "files already exist, no download")
        }
    }

    private fun loadStateData() {
        if (mRequestQueue.cache.get(STATE_WISE_DATA_URL) != null) {
            val serverDate = mRequestQueue.cache.get(STATE_WISE_DATA_URL).serverDate
            val calendar = Calendar.getInstance()
            if (DateUtils.getMinutesDifference(serverDate, calendar.timeInMillis) > 10) {
                mRequestQueue.cache.clear()
            }
        }
        initSortMap()
        state_wise_recycler_view.removeAllViews()
        toggleProgressBarUnion(View.VISIBLE)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            STATE_WISE_DATA_URL,
            null,
            Response.Listener { response -> loadMainData(response) },
            Response.ErrorListener { error ->
                Log.e(TAG, "VolleyError: ", error)
                toggleProgressBarUnion(View.GONE)
                alert_RL.visibility = View.VISIBLE
                pull_to_refresh.isRefreshing = false
            }
        )
        mRequestQueue.add(jsonArrayRequest)
    }

    private fun loadMainData(stateResponse: JSONArray?) {
        statewiseResponse = Gson().fromJson(stateResponse.toString(), StatewiseResponse::class.java)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            Constant.MAIN_RAW_DATA_URL,
            null,
            Response.Listener { mainResponse -> displayData(mainResponse) },
            Response.ErrorListener { error ->
                Log.e(TAG, "VolleyError: ", error)
                toggleProgressBarUnion(View.GONE)
                alert_RL.visibility = View.VISIBLE
                pull_to_refresh.isRefreshing = false
            })
        mRequestQueue.add(jsonObjectRequest)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivityReceiver)
        mActivityMinimized = true
        storeMainResponseForOfflineData(mainResponse)
    }

    private fun displayData(response: JSONObject?) {
        pull_to_refresh.isRefreshing = false
        toggleProgressBarUnion(View.GONE)
        mainResponse = Gson().fromJson(response.toString(), MainResponse::class.java)
        val lastUpdatedTime = mainResponse.statewise.first().lastupdatedtime
        inflateTimeView(lastUpdatedTime)
        displayTotalStats(mainResponse.statewise.first())
        displayStateWiseData(mainResponse.statewise)
    }

    private fun displayStateWiseData(statewise: List<Statewise>) {
        mStatewiseModified = statewise.drop(1)
        mStatewiseModified =
            mStatewiseModified.sortedWith(compareByDescending { it.confirmed.toInt() })
        inflateRecyclerView()
        initializeSortButtons()
    }

    private fun inflateRecyclerView() {
        adapter = object : ExpandableRecyclerViewAdapter(addElements()) {
            override fun getViewHolder(resource: Int, parent: ViewGroup?): ExpandableViewHolder {
                when (resource) {
                    R.layout.item_state -> return StateViewHolder(parent, resource)
                    R.layout.item_district -> return DistrictViewHolder(parent, resource)
                }
                return StateViewHolder(parent, resource)
            }

        }
        state_wise_recycler_view.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            this.adapter = adapter
        }
        adapter.notifyDataSetChanged()
        state_wise_recycler_view.itemAnimator = DefaultItemAnimator()
        state_wise_recycler_view.swapAdapter(adapter, false)
    }

    private fun addElements(): ArrayList<ExpandableAttribute>? {
        val parent = ArrayList<ExpandableAttribute>()
        var stateCount = 0
        mStatewiseModified.forEach {
            if (it.confirmed.toInt() > 0) {
                val state = ExpandableStates(
                    R.layout.item_state,
                    it.state,
                    it.confirmed.toInt(),
                    it.deltaconfirmed.toInt(),
                    it.active.toInt(),
                    it.recovered.toInt(),
                    it.deltarecovered.toInt(),
                    it.deaths.toInt(),
                    it.deltadeaths.toInt(),
                    stateCount++
                )
                var districts: List<DistrictData> =
                    Extractor.getDistrictData(statewiseResponse, it.state)
                districts = districts.sortedDescending()
                var count = 0
                state.addChild(ChildDistrict(R.layout.item_district, "DISTRICT", -1, 0, -1))
                districts.forEach { it2 ->
                    val district =
                        ChildDistrict(
                            R.layout.item_district,
                            it2.district,
                            it2.confirmed,
                            it2.delta.confirmed,
                            count++
                        )
                    state.addChild(district)
                }

                state.setExpand(false)
                parent.add(state)
            }
        }
        return parent
    }

    /**
     * display total stats
     */
    private fun displayTotalStats(data: Statewise) {
        confirm_total_count.text = data.confirmed
        confirm_delta.text = PlaceHolder.getDeltaString(data.deltaconfirmed)

        active_total_count.text = data.active

        recovered_total_count.text = data.recovered
        recovered_delta.text = PlaceHolder.getDeltaString(data.deltarecovered)

        deceased_total_count.text = data.deaths
        deceased_delta.text = PlaceHolder.getDeltaString(data.deltadeaths)


        active_percentage.text = Extractor.getPercentage(data.confirmed, data.active)
        recovered_percentage.text = Extractor.getPercentage(data.confirmed, data.recovered)
        deceased_percentage.text = Extractor.getPercentage(data.confirmed, data.deaths)
    }

    /**
     * Method to update date time textview at the top.
     */
    private fun inflateTimeView(data: String) {
        val dateString = DateUtils.toSimpleString(date = data)
        date = DateUtils.stringToDate(data)
        val ago = DateUtils.calculateAgoTime(Calendar.getInstance(), date)
        last_update_time_tv.text = PlaceHolder.getDateString(dateString, ago)
    }

    private fun toggleProgressBarUnion(toggle: Int) {
        progress_bar_time.visibility = toggle
        progress_bar_confirm.visibility = toggle
        progress_bar_active.visibility = toggle
        progress_bar_recovered.visibility = toggle
        progress_bar_deceased.visibility = toggle
        progress_bar_list.visibility = toggle
        if (View.VISIBLE == toggle) {
            confirm_total_count.text = ""
            active_total_count.text = ""
            recovered_total_count.text = ""
            deceased_total_count.text = ""

            confirm_delta.text = ""
            active_delta.text = ""
            recovered_delta.text = ""
            deceased_delta.text = ""

            last_update_time_tv.text = ""

            mStatewiseModified = emptyList()
            inflateRecyclerView()

            alert_RL.visibility = View.GONE
        }
    }

    private fun initializeSortButtons() {
        state_name_column.setOnClickListener {
            sortRecyclerView(state_name_column.text.toString())
        }
        state_confirmed_column.setOnClickListener {
            sortRecyclerView(state_confirmed_column.text.toString())
        }
        state_active_column.setOnClickListener {
            sortRecyclerView(state_active_column.text.toString())
        }
        state_recovered_column.setOnClickListener {
            sortRecyclerView(state_recovered_column.text.toString())
        }
        state_deceased_column.setOnClickListener {
            sortRecyclerView(state_deceased_column.text.toString())
        }
    }

    private fun sortRecyclerView(column: String) {
        when (column) {
            state_name_column.text.toString() -> mStatewiseModified = if (sortMap[column]!!) {
                mStatewiseModified.sortedBy { statewise -> statewise.state }
            } else {
                mStatewiseModified.sortedByDescending { statewise -> statewise.state }
            }


            state_confirmed_column.text.toString() -> mStatewiseModified = if (sortMap[column]!!) {
                mStatewiseModified.sortedBy { statewise -> statewise.confirmed.toInt() }
            } else {
                mStatewiseModified.sortedByDescending { statewise -> statewise.confirmed.toInt() }
            }


            state_active_column.text.toString() -> mStatewiseModified = if (sortMap[column]!!) {
                mStatewiseModified.sortedBy { statewise -> statewise.active.toInt() }
            } else {
                mStatewiseModified.sortedByDescending { statewise -> statewise.active.toInt() }
            }


            state_recovered_column.text.toString() -> mStatewiseModified = if (sortMap[column]!!) {
                mStatewiseModified.sortedBy { statewise -> statewise.recovered.toInt() }
            } else {
                mStatewiseModified.sortedByDescending { statewise -> statewise.recovered.toInt() }
            }


            state_deceased_column.text.toString() -> mStatewiseModified = if (sortMap[column]!!) {
                mStatewiseModified.sortedBy { statewise -> statewise.deaths.toInt() }
            } else {
                mStatewiseModified.sortedByDescending { statewise -> statewise.deaths.toInt() }
            }
        }
        sortMap[column] = !sortMap[column]!!
        inflateRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        info_btn.isEnabled = true
        settings_btn.isEnabled = true
        registerReceiver(
            connectivityReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        ConnectivityReceiver.connectivityReceiverListener = this
        mActivityMinimized = false
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        mInternetAvailable = isConnected
        if (isConnected) {
            pull_to_refresh.isEnabled = true
            alert_RL.visibility = View.GONE
            internet_text_view.visibility = View.GONE
            //loadStateData()
        } else {
            toggleProgressBarUnion(View.GONE)
            internet_text_view.visibility = View.VISIBLE
            pull_to_refresh.isEnabled = false
        }
    }

    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
        Log.d("notifications: ", "inside receiveREsult")
        if (resultCode == 1 && mActivityMinimized && !isNight()) {
            sendNotification()
        }
    }

    private fun sendNotification() {
        Log.d("notifications: ", "inside send notifications")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            Constant.MAIN_RAW_DATA_URL,
            null,
            Response.Listener { mainResponse -> buildDataForNotification(mainResponse) },
            Response.ErrorListener { error ->
                Log.e(TAG, "VolleyError: ", error)
                toggleProgressBarUnion(View.GONE)
                alert_RL.visibility = View.VISIBLE
                pull_to_refresh.isRefreshing = false
            })
        mRequestQueue.add(jsonObjectRequest)
    }

    private fun buildDataForNotification(mainResponse: JSONObject?) {
        val preferredState = AppSharedPreferenceHelper.getValueAsString(USER_STATE_NAME)
        val oldMainResponse = AppSharedPreferenceHelper.getStoredMainResponse() ?: return
        val newMainResponse = Gson().fromJson(mainResponse.toString(), MainResponse::class.java)

        val newConfirmTotal =
            newMainResponse.statewise.first().confirmed.toInt() -
                    oldMainResponse.statewise.first().confirmed.toInt()

        val newRecoveredTotal =
            newMainResponse.statewise.first().recovered.toInt() -
                    oldMainResponse.statewise.first().recovered.toInt()

        val newDeathTotal =
            newMainResponse.statewise.first().deaths.toInt() -
                    oldMainResponse.statewise.first().deaths.toInt()

        var preferredStateOldItem = Statewise()
        var preferredStateNewItem = Statewise()

        oldMainResponse.statewise.forEach {
            if (it.state == preferredState)
                preferredStateOldItem = it
        }
        newMainResponse.statewise.forEach {
            if (it.state == preferredState)
                preferredStateNewItem = it
        }
        val newConfirmState =
            preferredStateNewItem.confirmed.toInt() - preferredStateOldItem.confirmed.toInt()
        val newRecoveredState =
            preferredStateNewItem.recovered.toInt() - preferredStateOldItem.recovered.toInt()
        val newDeathState =
            preferredStateNewItem.deaths.toInt() - preferredStateOldItem.deaths.toInt()

        pushNotification(
            newConfirmTotal,
            newRecoveredTotal,
            newDeathTotal,
            newConfirmState,
            newRecoveredState,
            newDeathState, newMainResponse
        )
    }

    private fun pushNotification(
        newConfirmTotal: Int,
        newRecoveredTotal: Int,
        newDeathTotal: Int,
        newConfirmState: Int,
        newRecoveredState: Int,
        newDeathState: Int,
        newMainResponse: MainResponse
    ) {
        Log.d(
            "notifications: ",
            "$newConfirmTotal $newRecoveredTotal $newDeathTotal"
        )
        val countryTotalUpdate =
            PlaceHolder.getCountryTotal(newConfirmTotal, newRecoveredTotal, newDeathTotal)
        val stateTotalUpdate =
            PlaceHolder.getStateTotal(newConfirmState, newRecoveredState, newDeathState)
        Log.d("notifications: ", "$countryTotalUpdate % $stateTotalUpdate")

        val random = Random()
        val notificationID = random.nextInt(9999 - 1000) + 1000
        val notificationIntent = Intent(this, MainActivity::class.java)

        notificationIntent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP
                or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val intent: PendingIntent = PendingIntent.getActivity(
            this, 0,
            notificationIntent, 0
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(
                "com.indiacovid19.trackingapp",
                "Covid-19 update",
                "Covid-19 update"
            )
            if (countryTotalUpdate.isNotEmpty()) {
                val channelID = "com.indiacovid19.trackingapp"
                val notification = Notification.Builder(
                    this,
                    channelID
                )
                    .setContentTitle("Country update (" + DateUtils.getCurrentTime() + ")")
                    .setStyle(Notification.BigTextStyle().bigText(countryTotalUpdate))
                    .setContentText(countryTotalUpdate)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setChannelId(channelID)
                    .build().apply {
                        contentIntent = intent
                    }


                notificationManager?.notify(notificationID + 1, notification)
                storeMainResponseForOfflineData(newMainResponse)
            }
            if (stateTotalUpdate.isNotEmpty()) {
                val preferredStateName = AppSharedPreferenceHelper.getValueAsString(USER_STATE_NAME)
                val channelID = "com.indiacovid19.trackingapp"
                val notification = Notification.Builder(
                    this,
                    channelID
                )
                    .setContentTitle("$preferredStateName update (" + DateUtils.getCurrentTime() + ")")
                    .setStyle(Notification.BigTextStyle().bigText(stateTotalUpdate))
                    .setContentText(stateTotalUpdate)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setChannelId(channelID)
                    .build().apply {
                        contentIntent = intent
                    }

                notificationManager?.notify(notificationID + 2, notification)
                storeMainResponseForOfflineData(newMainResponse)
            }
        } else {

            if (countryTotalUpdate.isNotEmpty()) {
                val notification: Notification = Notification.Builder(this)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setContentTitle("Country update")
                    .setStyle(Notification.BigTextStyle().bigText(countryTotalUpdate))
                    .setContentText(countryTotalUpdate)
                    .setSmallIcon(R.drawable.ic_notif_icon)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC).build().apply {
                        contentIntent = intent
                    }
                storeMainResponseForOfflineData(newMainResponse)
                notificationManager!!.notify(notificationID + 1, notification)
            }
            if (stateTotalUpdate.isNotEmpty()) {
                val preferredStateName = AppSharedPreferenceHelper.getValueAsString(USER_STATE_NAME)
                val notification: Notification = Notification.Builder(this)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setContentTitle("$preferredStateName update")
                    .setStyle(Notification.BigTextStyle().bigText(stateTotalUpdate))
                    .setContentText(stateTotalUpdate)
                    .setSmallIcon(R.drawable.ic_notif_icon)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC).build().apply {
                        contentIntent = intent
                    }
                storeMainResponseForOfflineData(newMainResponse)
                notificationManager!!.notify(notificationID + 2, notification)
            }
        }
        Log.d("notifications: ", "end of function")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        id: String, name: String,
        description: String
    ) {

        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance)

        channel.description = description
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(true)
        channel.vibrationPattern =
            longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationManager?.createNotificationChannel(channel)
    }

    private fun downloadAndUnzipContent() {
        val download =
            DownloadFileAsync(
                downloadDir + GRAPHICS_ASSET_ZIP,
                object : DownloadFileAsync.PostDownload {
                    override fun downloadDone(fd: File?) {
                        download_progress_bar.visibility = View.GONE
                        progress_bar_text.visibility = View.GONE
                        if (fd != null) {
                            Log.i(TAG, "file download completed: " + fd.absolutePath)
                        }
                        val unzip = Unzipper(fd, downloadDir)
                        unzip.unzip()
                        Log.i(TAG, "file unzip completed")
                        AppSharedPreferenceHelper.instance()?.putValue(
                            GRAPHICS_DOWNLOAD_STATUS, true
                        )
                    }

                    override fun downloadProgress(value: String?) {
                        if (value != null) {
                            download_progress_bar.visibility = View.VISIBLE
                            progress_bar_text.visibility = View.VISIBLE
                            download_progress_bar.progress = value.toInt()
                        }
                    }
                })
        download.execute(GRAPHICS_ASSET_LINK)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            11 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    AppSharedPreferenceHelper.putValue(PERMISSION_STATUS, true)
                    downloadInitialization()
                } else {
                    AppSharedPreferenceHelper.putValue(PERMISSION_STATUS, false)
                    val snackbar: Snackbar =
                        Snackbar.make(
                            main_activity_RL,
                            "Some functionality may not work.\nEnable storage permission in Settings",
                            Snackbar.LENGTH_LONG
                        )
                    snackbar.setTextColor(resources.getColor(R.color.red))
                    snackbar.show()
                }
                return
            }

            else -> {
            }
        }
    }

    private fun showDisclaimer() {
        val viewGroup: ViewGroup = findViewById(android.R.id.content)
        val dialogView: View =
            LayoutInflater.from(this).inflate(R.layout.layout_disclaimer_dialog, viewGroup, false)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setView(dialogView)

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
        val states = loadStateDropDownList()
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.item_state_spinner, states)
        adapter.setDropDownViewResource(R.layout.item_state_spinner);
        dialogView.state_spinner.adapter = adapter
        dialogView.state_spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    dialogView.accept.isEnabled = true
                }
                if (position == 0) {
                    dialogView.accept.isEnabled = false
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }
        dialogView.accept.setOnClickListener {
            AppSharedPreferenceHelper.putValue(DISCLAIMER_STATUS, true)
            AppSharedPreferenceHelper.putValue(REFRESH_INTERVAL, loadRefreshInterval().first())
            alertDialog.dismiss()
            val userStateName = dialogView.state_spinner.selectedItem.toString()
            AppSharedPreferenceHelper.putValue(USER_STATE_NAME, userStateName)
            registerDevice(userStateName)
            startLoadingData()
        }
        dialogView.decline.setOnClickListener {
            finish()
        }
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setOnCancelListener {
            finish()
        }
    }

    private fun registerDevice(selectedItem: String?) {
        val registerStringRequest = StringRequest(
            DEVICE_STATE_REGISTER + selectedItem,
            Response.Listener { response ->
                if (response == "SUCCESS")
                    AppSharedPreferenceHelper.putValue(USER_DEVICE_REGISTERED, false)
                else
                    AppSharedPreferenceHelper.putValue(USER_DEVICE_REGISTERED, true)
            },
            Response.ErrorListener {
                AppSharedPreferenceHelper.putValue(USER_DEVICE_REGISTERED, true)
            })
        mRequestQueue.add(registerStringRequest)
    }

    private fun initSortMap() {
        sortMap[state_name_column.text.toString()] = true
        sortMap[state_confirmed_column.text.toString()] = true
        sortMap[state_active_column.text.toString()] = true
        sortMap[state_recovered_column.text.toString()] = true
        sortMap[state_deceased_column.text.toString()] = true
    }

    private fun invokeBackgroundService() {
        val intent = Intent(applicationContext, Covid19RefreshService::class.java)
        resultReceiver = CustomResultReceiver(Handler(), this)
        intent.putExtra("receiver", resultReceiver)
        stopService(intent)
        startService(intent)
        Log.d("notifications: ", "service started")
    }

    private fun storeMainResponseForOfflineData(mainResponse: MainResponse) {
        AppSharedPreferenceHelper.storeMainResponse(MAIN_RESPONSE, mainResponse)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (resultReceiver != null) {
            resultReceiver = null
        }
        Log.d("notifications: ", "activity destroyed")
    }
}

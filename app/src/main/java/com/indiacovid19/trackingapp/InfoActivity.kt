package com.indiacovid19.trackingapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.indiacovid19.trackingapp.adapter.AboutAdapter
import com.indiacovid19.trackingapp.adapter.MythBusterGraphicsAdapter
import com.indiacovid19.trackingapp.helper.AppSharedPreferenceHelper
import com.indiacovid19.trackingapp.helper.Constant.ABOUT_JSON_PATH
import com.indiacovid19.trackingapp.helper.Constant.DOWNLOAD_DIR
import com.indiacovid19.trackingapp.helper.Constant.MYTH_BUSTER_GRAPHICS_ASSET_PATH
import com.indiacovid19.trackingapp.helper.Constant.PERMISSION_STATUS
import com.indiacovid19.trackingapp.model.AboutResponse
import com.indiacovid19.trackingapp.util.DownloadUtils
import kotlinx.android.synthetic.main.activity_info.*
import java.util.*
import kotlin.collections.HashMap


class InfoActivity : AppCompatActivity() {

    private val layoutVisibilityMap: HashMap<Int, Boolean> = HashMap()
    private var downloadDir: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        this.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right)
        downloadDir = intent.getStringExtra(DOWNLOAD_DIR)
        loadAbout()
        initializeLayoutVisibilityMap()
        initializeButtonClicks()
        if (AppSharedPreferenceHelper.getValueAsBoolean(PERMISSION_STATUS)!!) {
            inflateMythBusters()
        } else {
            showNoPermissionGiven()
        }
    }

    private fun showNoPermissionGiven() {
        myth_buster_tv.visibility = View.VISIBLE
        myth_buster_viewPager.visibility = View.GONE
        myth_buster_error.visibility = View.VISIBLE
    }

    private fun initializeButtonClicks() {
        info_back_btn.setOnClickListener {
            destroyActivity()
        }
        about_title_tv.setOnClickListener {
            if (layoutVisibilityMap[about_recycler_view.id]!!) {
                about_recycler_view.visibility = View.VISIBLE
            } else {
                about_recycler_view.visibility = View.GONE
            }
            layoutVisibilityMap[about_recycler_view.id] =
                !layoutVisibilityMap[about_recycler_view.id]!!
        }
        myth_buster_tv.setOnClickListener {
            if (layoutVisibilityMap[myth_buster_viewPager.id]!!) {
                myth_buster_viewPager.visibility = View.VISIBLE
            } else {
                myth_buster_viewPager.visibility = View.GONE
            }
            layoutVisibilityMap[myth_buster_viewPager.id] =
                !layoutVisibilityMap[myth_buster_viewPager.id]!!
        }
    }

    private fun initializeLayoutVisibilityMap() {
        layoutVisibilityMap[myth_buster_viewPager.id] = false
        layoutVisibilityMap[about_recycler_view.id] = false
    }

    private fun loadAbout() {
        val aboutString = assets.open(ABOUT_JSON_PATH).bufferedReader().use {
            it.readText()
        }
        val faqData = Gson().fromJson(aboutString, AboutResponse::class.java).sortedBy { it.place }
        val adapter = AboutAdapter(faqData, this)
        about_recycler_view.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            this.adapter = adapter
        }
        adapter.notifyDataSetChanged()
    }

    private fun inflateMythBusters() {
        val imageList =
            DownloadUtils.getFilesInDirectory(downloadDir + MYTH_BUSTER_GRAPHICS_ASSET_PATH)
        if (imageList.isEmpty()) {
            myth_buster_layout.visibility = View.GONE
            return
        }
        myth_buster_layout.visibility = View.VISIBLE
        myth_buster_viewPager.adapter = MythBusterGraphicsAdapter(this, imageList)
        val timer = Timer()
        val task = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    if (myth_buster_viewPager.currentItem < imageList.size - 1) {
                        myth_buster_viewPager.currentItem = myth_buster_viewPager.currentItem + 1
                    } else {
                        myth_buster_viewPager.currentItem = 0
                    }
                }
            }
        }
        timer.schedule(task, 1000, 5000)
    }

    override fun onBackPressed() {
        destroyActivity()
    }

    private fun destroyActivity() {
        finish()
        this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
    }

}

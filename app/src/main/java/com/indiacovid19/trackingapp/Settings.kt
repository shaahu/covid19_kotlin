package com.indiacovid19.trackingapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.indiacovid19.trackingapp.helper.AppSharedPreferenceHelper
import com.indiacovid19.trackingapp.util.ActivityHelper
import kotlinx.android.synthetic.main.activity_settings.*

class Settings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
        initializeListeners()
        setupButtons()
    }

    private fun setupButtons() {
        dark_mode_switch.isChecked = !AppSharedPreferenceHelper.isLightModeOn()!!
    }

    private fun initializeListeners() {
        settings_back_btn.setOnClickListener {
            destroyActivity()
        }
        dark_mode_switch.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                AppSharedPreferenceHelper.setLightModeOn(false)
            } else {
                AppSharedPreferenceHelper.setLightModeOn(true)
            }
            ActivityHelper.setMode()
        }
        notification_btn.setOnClickListener {
            notification_btn.isEnabled = false
            val notifIntent = Intent(this, NotificationSettings::class.java)
            startActivity(notifIntent)
        }
        send_feedback_btn.setOnClickListener {
            send_feedback_btn.isEnabled = false
            val feedbackIntent = Intent(this, FeedbackActivity::class.java)
            startActivity(feedbackIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        notification_btn.isEnabled = true
        send_feedback_btn.isEnabled = true
    }

    override fun onBackPressed() {
        destroyActivity()
    }

    private fun destroyActivity() {
        finish()
        this.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right)
    }
}

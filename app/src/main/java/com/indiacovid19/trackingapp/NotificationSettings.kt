package com.indiacovid19.trackingapp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.indiacovid19.trackingapp.helper.AppSharedPreferenceHelper
import com.indiacovid19.trackingapp.helper.Constant.NOTIFICATION_ENABLED
import com.indiacovid19.trackingapp.helper.Constant.REFRESH_INTERVAL
import com.indiacovid19.trackingapp.helper.Constant.USER_STATE_NAME
import com.indiacovid19.trackingapp.util.ActivityHelper.loadRefreshInterval
import com.indiacovid19.trackingapp.util.ActivityHelper.loadStateDropDownList
import kotlinx.android.synthetic.main.activity_notification_settings.*

class NotificationSettings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_settings)
        this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
        setupViews()
        setupListeners()
    }

    private fun setupListeners() {
        notif_back_btn.setOnClickListener {
            destroyActivity()
        }

        notification_enable_switch.setOnClickListener {
            val notificationEnabled = notification_enable_switch.isChecked
            if (notificationEnabled) {
                notification_settings_RL.visibility = View.VISIBLE
            } else {
                notification_settings_RL.visibility = View.GONE
            }
            AppSharedPreferenceHelper.putValue(NOTIFICATION_ENABLED, notificationEnabled)
        }

        preferred_state_spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    AppSharedPreferenceHelper.putValue(
                        USER_STATE_NAME,
                        preferred_state_spinner.selectedItem.toString()
                    )
                }

            }
        refresh_interval_spn.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    AppSharedPreferenceHelper.putValue(
                        REFRESH_INTERVAL,
                        refresh_interval_spn.selectedItem.toString()
                    )
                }

            }
    }

    private fun setupViews() {
        val notificationEnabled =
            AppSharedPreferenceHelper.getValueAsBoolean(NOTIFICATION_ENABLED, true)!!
        if (notificationEnabled) {
            notification_settings_RL.visibility = View.VISIBLE
        } else {
            notification_settings_RL.visibility = View.GONE
        }
        notification_enable_switch.isChecked = notificationEnabled

        val states = loadStateDropDownList().drop(1)
        val stateAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.item_state_spinner, states)
        stateAdapter.setDropDownViewResource(R.layout.item_state_spinner);
        preferred_state_spinner.adapter = stateAdapter
        val preferredState = AppSharedPreferenceHelper.getValueAsString(USER_STATE_NAME)
        preferred_state_spinner.setSelection(states.indexOf(preferredState))

        val intervals = loadRefreshInterval()
        val intervalAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.item_state_spinner, intervals)
        intervalAdapter.setDropDownViewResource(R.layout.item_state_spinner);
        refresh_interval_spn.adapter = intervalAdapter
        val interval = AppSharedPreferenceHelper.getValueAsString(REFRESH_INTERVAL)
        refresh_interval_spn.setSelection(intervals.indexOf(interval))
    }

    override fun onBackPressed() {
        destroyActivity()
    }

    private fun destroyActivity() {
        finish()
        this.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right)
    }
}

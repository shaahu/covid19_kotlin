package com.indiacovid19.trackingapp

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.indiacovid19.trackingapp.helper.Constant.FEEDBACK_POST_URL
import kotlinx.android.synthetic.main.activity_feedback.*


class FeedbackActivity : AppCompatActivity() {

    private var mRequestQueue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
        setupListeners()
        mRequestQueue = Volley.newRequestQueue(this)
    }

    private fun setupListeners() {
        feedback_back_btn.setOnClickListener {
            destroyActivity()
        }
        feedback_submit_btn.setOnClickListener {
            if (validation()) {
                sendFeedback()
            }
        }
    }

    private fun validation(): Boolean {
        if (!isValidEmail(email_feedback.text)) {
            Toast.makeText(this, "Email address is invalid", Toast.LENGTH_LONG).show()
            return false
        }
        if (name_feedback.text.isEmpty()) {
            Toast.makeText(this, "Name is empty", Toast.LENGTH_LONG).show()
            return false
        }
        if (message_body_feedback.text.isEmpty()) {
            Toast.makeText(this, "Message body is empty", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun sendFeedback() {
        val name = name_feedback.text.toString()
        val email = email_feedback.text.toString()
        val body = message_body_feedback.text.toString()
        val sendFeedbackStringRequest = StringRequest(
            "$FEEDBACK_POST_URL?name=$name&email=$email&body=$body",
            Response.Listener { response ->
                Log.d("feedback", response.toString())
                if (response == "success") {
                    val snackbar: Snackbar =
                        Snackbar.make(
                            feedback_RL,
                            "Thank you for your valuable feedback!",
                            Snackbar.LENGTH_LONG
                        )
                    snackbar.show()
                    clearAndDisable()
                }
            },
            Response.ErrorListener {
                Log.d("feedback", it.toString())
            })
        mRequestQueue?.add(sendFeedbackStringRequest)

    }

    private fun clearAndDisable() {
        feedback_submit_btn.isEnabled = false
        name_feedback.text.clear()
        email_feedback.text.clear()
        message_body_feedback.text.clear()
    }

    override fun onBackPressed() {
        destroyActivity()
    }

    private fun destroyActivity() {
        finish()
        this.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right)
    }

    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target)
            .matches()
    }
}

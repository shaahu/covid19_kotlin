package com.indiacovid19.trackingapp.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.indiacovid19.trackingapp.R

/**
 * Created by Shahu Ronghe on 03, April, 2020
 * in Covid-19 India Stats App
 */
class ArchiaBoldTextView : AppCompatTextView {
    private var mContext: Context

    constructor(context: Context) : super(context) {
        mContext = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mContext = context
        init()
    }

    private fun init() {
        val typeface = ResourcesCompat.getFont(mContext, R.font.archia_bold)
        this.typeface = typeface
    }
}
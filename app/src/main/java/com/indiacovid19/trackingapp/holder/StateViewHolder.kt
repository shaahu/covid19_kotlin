package com.indiacovid19.trackingapp.holder

import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.indiacovid19.trackingapp.R
import com.indiacovid19.trackingapp.adapter_model.ExpandableStates
import com.indiacovid19.trackingapp.helper.ExpandableAttribute
import kotlin.text.Typography.nbsp

/**
 * Created by Shahu Ronghe on 04, April, 2020
 * in Covid-19 India Stats App
 */
class StateViewHolder(parent: ViewGroup?, resource: Int) :
    ExpandableViewHolder(parent, resource) {
    private var stateName: TextView? = null
    private var stateConfirmed: TextView? = null
    private var stateActive: TextView? = null
    private var stateRecovered: TextView? = null
    private var stateDeceased: TextView? = null


    override fun bindIds(itemView: View?) {
        stateName = itemView!!.findViewById<View>(R.id.state_name) as TextView
        stateConfirmed = itemView.findViewById<View>(R.id.state_confirmed) as TextView
        stateActive = itemView.findViewById<View>(R.id.state_active) as TextView
        stateRecovered = itemView.findViewById<View>(R.id.state_recovered) as TextView
        stateDeceased = itemView.findViewById<View>(R.id.state_deceased) as TextView
    }

    override fun setData(data: ExpandableAttribute?) {
        val pos = ((data as ExpandableStates?)!!.stateCount)
        if (pos % 2 != 0) {
            itemView.setBackgroundResource(R.drawable.rounded_gray_bg)
        } else {
            itemView.setBackgroundResource(R.drawable.rounded_white_bg)
        }

        stateName?.text = ((data as ExpandableStates?)?.stateName)
        if (((data as ExpandableStates?)!!.deltaConfirmed) > 0) {
            stateConfirmed?.text =
                Html.fromHtml(" <small><font color='#FF6B89'>+" + ((data as ExpandableStates?)?.deltaConfirmed) + "</font></small>$nbsp" + data?.confirmedCount.toString())
        } else {
            stateConfirmed?.text = data?.confirmedCount.toString()
        }

        stateActive?.text = (data?.activeCount.toString())

        if (((data as ExpandableStates?)!!.deltaRecovered) > 0) {
            stateRecovered?.text =
                Html.fromHtml(" <small><font color='#7FCA8F'>+" + ((data as ExpandableStates?)?.deltaRecovered) + "</font></small>$nbsp" + data?.recoveredCount.toString())
        } else {
            stateRecovered?.text = data?.recoveredCount.toString()
        }

        if (((data as ExpandableStates?)!!.deltaDeceased) > 0) {
            stateDeceased?.text =
                Html.fromHtml(" <small><font color='#A7ACB1'>+" + ((data as ExpandableStates?)?.deltaDeceased) + "</font></small>$nbsp" + data?.deceasedCount.toString())
        } else {
            stateDeceased?.text = data?.deceasedCount.toString()
        }
    }

    override fun onCollapse(data: ExpandableAttribute?) {
        // do on Collapse the view
    }

    override fun onExpand(data: ExpandableAttribute?) {}
}
package com.indiacovid19.trackingapp.holder

import android.text.Html
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.indiacovid19.trackingapp.R
import com.indiacovid19.trackingapp.adapter_model.ChildDistrict
import com.indiacovid19.trackingapp.helper.ExpandableAttribute


/**
 * Created by Shahu Ronghe on 04, April, 2020
 * in Covid-19 India Stats App
 */
class DistrictViewHolder(parent: ViewGroup?, resource: Int) :
    ExpandableViewHolder(parent, resource) {
    private var districtName: TextView? = null
    private var districtConfirmedCount: TextView? = null
    private var districtDeltaConfirmed: TextView? = null
    private var districtCountRL: RelativeLayout? = null
    override fun bindIds(itemView: View?) {
        districtName = itemView!!.findViewById<View>(R.id.district_name) as TextView
        districtConfirmedCount =
            itemView.findViewById<View>(R.id.district_confirmed_count) as TextView
        districtDeltaConfirmed =
            itemView.findViewById<View>(R.id.district_confirmed_delta) as TextView
        districtCountRL =
            itemView.findViewById<View>(R.id.district_count_RL) as RelativeLayout
    }

    override fun setData(data: ExpandableAttribute?) {
        val pos = (data as ChildDistrict?)!!.count
        if (pos == -1) {
            inflateDistrictHeader()
            return
        }
        if (pos % 2 != 0) {
            districtCountRL!!.setBackgroundResource(R.drawable.rounded_gray_bg_district)
            districtName!!.setBackgroundResource(R.drawable.rounded_gray_bg_district)
        } else {
            districtCountRL!!.setBackgroundResource(R.drawable.rounded_white_bg_district)
            districtName!!.setBackgroundResource(R.drawable.rounded_white_bg_district)
        }
        districtName!!.text = (data as ChildDistrict?)!!.districtName
        districtConfirmedCount!!.text = (data as ChildDistrict?)!!.districtConfirmedCount.toString()
        if ((data as ChildDistrict?)!!.delta > 0) {
            districtDeltaConfirmed!!.visibility = View.VISIBLE
            districtDeltaConfirmed!!.text = "+" + (data as ChildDistrict?)!!.delta.toString()
        } else {
            districtDeltaConfirmed!!.visibility = View.VISIBLE
            districtDeltaConfirmed!!.text = ""
        }
    }

    private fun inflateDistrictHeader() {
        districtCountRL!!.setBackgroundResource(R.drawable.columns_rounded_gray_bg)
        districtName!!.setBackgroundResource(R.drawable.columns_rounded_gray_bg)
        districtName!!.text = Html.fromHtml("<bold> DISTRICT </bold")
        districtConfirmedCount!!.text = Html.fromHtml("<bold> CONFIRMED </bold")
        districtName!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13F)
        districtConfirmedCount!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13F)
        districtDeltaConfirmed!!.visibility = View.VISIBLE
        districtDeltaConfirmed!!.text = ""
        districtConfirmedCount!!.setOnClickListener {  }
    }

    override fun onCollapse(data: ExpandableAttribute?) {}
    override fun onExpand(data: ExpandableAttribute?) {}
}
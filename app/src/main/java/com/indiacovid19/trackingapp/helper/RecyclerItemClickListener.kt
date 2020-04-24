package com.indiacovid19.trackingapp.helper

import android.view.View

import com.indiacovid19.trackingapp.holder.ExpandableViewHolder

/**
 * Created by Shahu Ronghe on 04, April, 2020
 * in Covid-19 India Stats App
 */
interface RecyclerItemClickListener {
    fun onItemClickListener(
        viewHolder: ExpandableViewHolder?,
        view: View?,
        position: Int
    )
}
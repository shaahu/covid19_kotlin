package com.indiacovid19.trackingapp.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.indiacovid19.trackingapp.helper.HolderProperty

/**
 * Created by Shahu Ronghe on 04, April, 2020
 * in Covid-19 India Stats App
 */

abstract class ExpandableViewHolder(parent: ViewGroup?, resource: Int) :
    RecyclerView.ViewHolder(LayoutInflater.from(parent?.context).inflate(resource, parent, false)),
    HolderProperty {
    init {
        bindIds(itemView)
    }
}
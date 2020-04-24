package com.indiacovid19.trackingapp.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.indiacovid19.trackingapp.helper.ExpandableAttribute
import com.indiacovid19.trackingapp.helper.RecyclerItemClickListener
import com.indiacovid19.trackingapp.holder.ExpandableViewHolder
import java.util.*

/**
 * Created by Shahu Ronghe on 03, April, 2020
 * in Covid-19 India Stats App
 */

abstract class ExpandableRecyclerViewAdapter(children: ArrayList<ExpandableAttribute>?) :
    RecyclerView.Adapter<ExpandableViewHolder?>() {
    private var children: ArrayList<ExpandableAttribute>?
    private var itemClickListener: RecyclerItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpandableViewHolder {
        return getViewHolder(viewType, parent)
    }

    override fun onBindViewHolder(holder: ExpandableViewHolder, position: Int) {
        val attribute: ExpandableAttribute? = getViewOnPosition(position)
        holder.setData(attribute)
        holder.itemView.tag = attribute
        if (itemClickListener != null) {
            holder.itemView.isClickable = true
        }
        holder.itemView.setOnClickListener { v ->
            val attribute2: ExpandableAttribute = v.tag as ExpandableAttribute
            itemClickListener?.onItemClickListener(
                holder,
                holder.itemView,
                holder.adapterPosition
            )
            if (!attribute2.isDoNotCollapse()) {
                if (attribute2.isExpand()) {
                    setCollapse(attribute, holder.adapterPosition)
                    attribute2.setExpand(false)
                    holder.onCollapse(attribute)
                } else {
                    attribute2.setExpand(true)
                    setExpand(attribute, holder.adapterPosition)
                    holder.onExpand(attribute)
                }
            }
        }
    }

    private fun setExpand(attribute: ExpandableAttribute?, position: Int) {
        if (attribute != null) {
            for (i in 1..attribute.countList) {
                notifyItemInserted(position + i)
            }
        }
    }

    private fun setCollapse(attribute: ExpandableAttribute?, position: Int) {
        val count = 0
        //TODO: need to work on it
        if (attribute != null) {
            for (i in attribute.countList downTo 1) {
                Log.d("Collapse", count.toString() + "")
                notifyItemRemoved(position + i)
            }
        }
    }

    override fun getItemCount(): Int {
        var count = 0
        for (child in children!!) count += child.countList
        count += children!!.size
        return count
    }

    override fun getItemViewType(position: Int): Int {
        val attribute: ExpandableAttribute? = getViewOnPosition(position)
        return attribute?.getResourceId() ?: 0
    }

    fun getItemClickListener(): RecyclerItemClickListener? {
        return itemClickListener
    }

    fun setItemClickListener(itemClickListener: RecyclerItemClickListener?) {
        this.itemClickListener = itemClickListener
    }

    fun addChild(child: ExpandableAttribute) {
        if (children == null) children = ArrayList<ExpandableAttribute>()
        children!!.add(child)
    }

    fun removeChild(index: Int) {
        if (children == null) return
        children!!.removeAt(index)
    }

    fun removeChild(child: ExpandableAttribute) {
        if (children == null) return
        children!!.remove(child)
    }

    fun addChild(index: Int, child: ExpandableAttribute) {
        children!!.add(index, child)
    }

    private fun getViewOnPosition(position: Int): ExpandableAttribute? {
        val count = arrayOf(-1)
        for (expandableChild in children!!) {
            if (count[0] == position) return expandableChild
            val child: ExpandableAttribute? = getChild(expandableChild, count, position)
            if (child != null) return child
            //            else   // this code working only for one child not for multiple Child and there childs
//                count[0] += expandableChild.getCountList() + 1;
        }
        return null
    }

    private fun getChild(
        child: ExpandableAttribute,
        count: Array<Int>,
        position: Int
    ): ExpandableAttribute? {
        count[0]++
        if (count[0] == position) return child
        if (child.hasChild() && child.isExpand()) for (expandableChild in child.children!!) {
            val child1: ExpandableAttribute? = getChild(expandableChild, count, position)
            if (child1 != null) return child1
            //                else  // same as above not worked for multiple child and their respective
//                    count[0]++;
        }
        return null
    }

    protected abstract fun getViewHolder(resource: Int, parent: ViewGroup?): ExpandableViewHolder

    init {
        this.children = children
    }
}
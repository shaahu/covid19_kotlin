package com.indiacovid19.trackingapp.helper

import java.util.*

/**
 * Created by Shahu Ronghe on 04, April, 2020
 * in Covid-19 India Stats App
 */
abstract class ExpandableAttribute protected constructor(private val resourceId: Int) :
    ExpandableProperty {
    private var isExpand = true
    private var hasChild = false
    private var doNotCollapse = false
    var children: ArrayList<ExpandableAttribute>? = null
        private set

    override fun isExpand(): Boolean {
        return isExpand
    }

    override fun hasChild(): Boolean {
        return hasChild
    }

    override fun setHasChild(hasChild: Boolean) {
        this.hasChild = hasChild
    }

    override fun doNotCollapse() {
        doNotCollapse = false
    }

    override fun isDoNotCollapse(): Boolean {
        return doNotCollapse
    }

    override fun setExpand(expand: Boolean) {
        isExpand = expand
        if (!isExpand) setExpandALL(false)
    }

    override fun getResourceId(): Int {
        return resourceId
    }

    fun addChild(child: ExpandableAttribute) {
        hasChild = true
        if (children == null) children =
            ArrayList()
        children!!.add(child)
    }

    fun removeChild(child: ExpandableAttribute) {
        if (children != null) {
            children!!.remove(child)
            hasChild = !children!!.isEmpty()
        }
    }

    private fun getCount(child: ExpandableAttribute): Int {
        var i = 0
        if (child.hasChild && child.isExpand && child.children != null) {
            for (expandableChild in child.children!!) i += getCount(
                expandableChild
            )
            return i + child.children!!.size
        }
        return i
    }

    private fun setExpandALL(isExpand: Boolean) {
        getChild(this, isExpand)
    }

    private fun getChild(attribute: ExpandableAttribute, isExpand: Boolean) {
        if (attribute.hasChild && attribute.children != null) {
            for (child in attribute.children!!) {
                getChild(child, isExpand)
            }
        }
        attribute.isExpand = isExpand
    }

    val countList: Int
        get() = getCount(this)

}
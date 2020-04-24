package com.indiacovid19.trackingapp.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.indiacovid19.trackingapp.R
import java.io.File
import java.io.IOException


/**
 * Created by Shahu Ronghe on 08, April, 2020
 * in Covid-19 India Stats App
 */
class MythBusterGraphicsAdapter(private var context: Context, items: Array<File>) :
    PagerAdapter() {
    private val TAG = javaClass.simpleName
    private var graphicsItems: Array<File> = items

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return graphicsItems.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_mythbuster_graphic, null)

        val graphicView = view.findViewById<View>(R.id.myth_buster_graphic_iv) as ImageView

        val imgPath = graphicsItems[position]
        try {
            val myBitmap = BitmapFactory.decodeFile(imgPath.absolutePath)
            graphicView.setImageBitmap(myBitmap)
        } catch (ex: IOException) {
            Log.e(TAG, "exception: ", ex)
        }

        val viewPager = container as ViewPager
        viewPager.addView(view, 0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }
}
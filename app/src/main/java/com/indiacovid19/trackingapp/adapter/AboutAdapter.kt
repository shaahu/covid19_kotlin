package com.indiacovid19.trackingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.indiacovid19.trackingapp.R
import com.indiacovid19.trackingapp.model.AboutResponseItem
import com.indiacovid19.trackingapp.views.ArchiaBoldTextView
import kotlinx.android.synthetic.main.item_faq.view.*

/**
 * Created by Shahu Ronghe on 08, April, 2020
 * in Covid-19 India Stats App
 */
class AboutAdapter(private val items: List<AboutResponseItem>, private val context: Context) :
    RecyclerView.Adapter<AboutAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_faq, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.faqQue.text = items[position].que
        holder.faqAns.text = items[position].ans
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val faqQue: ArchiaBoldTextView = view.faq_que
        val faqAns: ArchiaBoldTextView = view.faq_ans
    }
}
package com.example.news.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.news.R

class adapter (private val context: Activity, private val list: ArrayList<String>, private val imgid: ArrayList<Int>)
    : ArrayAdapter<String>(context, R.layout.adapter, list) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.adapter, null, false)

        val titleText = rowView.findViewById(R.id.title) as TextView
        val imageView = rowView.findViewById(R.id.icon) as ImageView

        titleText.text = list[position]
        imageView.setImageResource(imgid[position])
        return rowView
    }
}
package com.example.analytics.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.analytics.R

class SortOptionAdaptor(
    private var _context: Context, private var resource: Int,
    private var items: ArrayList<Pair<String, Int>>,
    private var selectedOption: Int
) : ArrayAdapter<Pair<String, Int>>(_context, resource, items) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(_context).inflate(resource, null)
        view.apply {
            if (items[position].second != selectedOption)
                findViewById<ImageView>(R.id.check).isVisible = false
            findViewById<TextView>(R.id.option).text = items[position].first
        }
        return view
    }
}
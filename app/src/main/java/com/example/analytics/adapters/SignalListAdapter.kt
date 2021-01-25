package com.example.analytics.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.analytics.R
import com.example.analytics.models.signal.SignalRaw
import com.example.analytics.utils.Signal
import java.text.SimpleDateFormat

class SignalListAdapter
    (
    private var _context: Context,
    private var resource: Int,
    private var items: List<SignalRaw>
) : ArrayAdapter<SignalRaw>(_context, resource, items) {

    @SuppressLint("SimpleDateFormat", "ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val formatter = SimpleDateFormat("dd MMM yyyy\nHH : mm : ss")

        val layoutInflater = LayoutInflater.from(_context)
        val view = layoutInflater.inflate(resource, null)
        val timeStamp = view.findViewById<TextView>(R.id.textView2)
        val strength = view.findViewById<ImageView>(R.id.imageView2)
        val general = view.findViewById<TextView>(R.id.textView3)
        val level = view.findViewById<TextView>(R.id.textView1)

        timeStamp.text = formatter.format(items[position].timeStamp)

        when (items[position].signal) {
            Signal.CELLULAR.ordinal -> {
                strength.setImageResource(getCellularImage(items[position].level))
                general.text = items[position].attribute
            }
            Signal.WIFI.ordinal -> {
                strength.setImageResource(getWifiImage(items[position].level))
                general.text = items[position].attribute + " Mbps"
            }
        }
        level.text = items[position].strength.toString() + " dbm"
        return view
    }

    private fun getWifiImage(level: Int): Int {
        return when (level) {
            0 -> R.drawable.ic_wifi_strength_0
            1 -> R.drawable.ic_wifi_strength_1
            2 -> R.drawable.ic_wifi_strength_2
            3 -> R.drawable.ic_wifi_strength_3
            else -> R.drawable.ic_wifi_strength_4
        }
    }

    private fun getCellularImage(level: Int): Int {
        return when (level) {
            0 -> R.drawable.ic_strength_0
            1 -> R.drawable.ic_strength_1
            2 -> R.drawable.ic_strength_2
            3 -> R.drawable.ic_strength_3
            else -> R.drawable.ic_strength_4
        }
    }
}
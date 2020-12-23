package com.example.androidDeviceDetails

import android.content.Context
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.androidDeviceDetails.adapters.BatteryListAdapter
import com.example.androidDeviceDetails.databinding.ActivityBatteryBinding
import com.example.androidDeviceDetails.managers.AppEntry
import java.util.*

class BatteryViewModel(private val batteryBinding: ActivityBatteryBinding, val context: Context) {
    fun onNoData() {
        batteryBinding.root.post {
            batteryBinding.batteryListView.adapter =
                BatteryListAdapter(context, R.layout.battery_tile, arrayListOf())
            batteryBinding.total.text = context.getString(R.string.no_usage_recorded)
        }
    }

    fun onData(appEntryList: ArrayList<AppEntry>, totalDrop: Int) {
        batteryBinding.root.post {
            batteryBinding.batteryListView.adapter =
                BatteryListAdapter(context, R.layout.battery_tile, appEntryList)
            val totalText = "Total drop is $totalDrop %"
            batteryBinding.total.text = totalText
        }
    }

    fun onDayWiseMode(v: TextView) {
        v.text = context.getString(R.string.day_wise)
        batteryBinding.batteryDatePicker.isVisible = true
    }

    fun onTillTodayMode(v: TextView) {
        v.text = context.getString(R.string.till_today)
        batteryBinding.batteryDatePicker.isVisible = false
    }


}
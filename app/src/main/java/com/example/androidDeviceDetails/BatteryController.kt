package com.example.androidDeviceDetails

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.AdapterView
import android.widget.TextView
import com.example.androidDeviceDetails.adapters.BatteryListAdapter
import com.example.androidDeviceDetails.databinding.ActivityBatteryBinding
import com.example.androidDeviceDetails.managers.AppBatteryUsageManager
import com.example.androidDeviceDetails.managers.AppEntry
import com.example.androidDeviceDetails.utils.Utils
import java.util.*

class BatteryController(val context: Context, private val batteryBinding: ActivityBatteryBinding) {
    private lateinit var calendar: Calendar
    private val batteryViewModel = BatteryViewModel(batteryBinding, context)

    fun setCooker(offset: Int = 0, reset: Boolean = false, tillToday: Boolean = false) {
        if (reset) calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar.add(Calendar.DAY_OF_MONTH, offset)
        batteryBinding.today.text = Utils.getDateString(calendar)
        AppBatteryUsageManager().cookBatteryData(
            onCookingDone,
            if (tillToday) 0 else calendar.timeInMillis,
            endTime = calendar.timeInMillis + 24 * 60 * 60 * 1000
        )
    }

    private val onCookingDone = object : ICookingDone {
        override fun onNoData() = batteryViewModel.onNoData()
        override fun onData(appEntryList: ArrayList<AppEntry>, totalDrop: Int) =
            batteryViewModel.onData(appEntryList, totalDrop)
    }

    fun redirectToAppInfo(parent: AdapterView<*>, position: Int) {
        val adapter = parent.adapter as BatteryListAdapter
        val item = adapter.getItem(position)
        val infoIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        infoIntent.addCategory(Intent.CATEGORY_DEFAULT)
        infoIntent.data = Uri.parse("package:${item?.packageId}")
        context.startActivity(infoIntent)
    }

    fun toggleCookingMode(v: TextView) {
        if (v.text == context.getString(R.string.till_today)) {
            batteryViewModel.onTillTodayMode(v)
            setCooker(offset = 0, reset = true)
        } else {
            batteryViewModel.onDayWiseMode(v)
            setCooker(offset = 0, reset = true, tillToday = true)
        }
    }
}
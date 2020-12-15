package com.example.androidDeviceDetails

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.example.androidDeviceDetails.adapters.BatteryListAdapter
import com.example.androidDeviceDetails.databinding.ActivityBatteryBinding
import com.example.androidDeviceDetails.managers.AppBatteryUsageManager
import com.example.androidDeviceDetails.utils.Utils
import java.util.*

@SuppressLint("SetTextI18n")
class BatteryActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var calendar: Calendar
    private lateinit var batteryBinding: ActivityBatteryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        batteryBinding = DataBindingUtil.setContentView(this, R.layout.activity_battery)
        batteryBinding.batteryDatePicker.isVisible = false
        batteryBinding.leftArrow.setOnClickListener(this)
        batteryBinding.rightArrow.setOnClickListener(this)
        batteryBinding.description.setOnClickListener(this)
        setCalender(offset = 0, reset = true, tillToday = true)
        batteryBinding.batteryListView.setOnItemClickListener { parent, _, position, _ ->
            redirectToAppInfo(parent, position)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.leftArrow -> setCalender(offset = -1)
            R.id.rightArrow -> setCalender(offset = 1)
            R.id.description -> {
                v as TextView
                if (v.text == "Till today") {
                    v.text = "Day wise"
                    batteryBinding.batteryDatePicker.isVisible = true
                    setCalender(offset = 0, reset = true)
                } else {
                    v.text = "Till today"
                    batteryBinding.batteryDatePicker.isVisible = false
                    setCalender(offset = 0, reset = true, tillToday = true)
                }
            }
        }
    }

    private fun setCalender(offset: Int = 0, reset: Boolean = false, tillToday: Boolean = false) {
        if (reset) calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar.add(Calendar.DAY_OF_MONTH, offset)
        batteryBinding.today.text =
            "${Utils.getWeek(calendar.get(Calendar.DAY_OF_WEEK))}, ${calendar.get(Calendar.DAY_OF_MONTH)} ${
                Utils.getMonth(calendar.get(Calendar.MONTH))
            }"
        AppBatteryUsageManager().cookBatteryData(
            context = this,
            batteryBinding,
            if (tillToday) 0 else calendar.timeInMillis,
            endTime = calendar.timeInMillis + 24 * 60 * 60 * 1000
        )
    }

    private fun redirectToAppInfo(parent: AdapterView<*>, position: Int) {
        val adapter = parent.adapter as BatteryListAdapter
        val item = adapter.getItem(position)
        val infoIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        infoIntent.addCategory(Intent.CATEGORY_DEFAULT)
        infoIntent.data = Uri.parse("package:${item?.packageId}")
        startActivity(infoIntent)
    }
}


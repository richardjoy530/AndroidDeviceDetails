package com.example.androidDeviceDetails

import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import com.example.androidDeviceDetails.managers.AppEntry
import com.example.androidDeviceDetails.utils.Utils
import java.util.*

class BatteryActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var calendar: Calendar
    private lateinit var batteryBinding: ActivityBatteryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        batteryBinding = DataBindingUtil.setContentView(this, R.layout.activity_battery)
        batteryBinding.leftArrow.setOnClickListener(this)
        batteryBinding.rightArrow.setOnClickListener(this)
        batteryBinding.description.setOnClickListener(this)
        batteryBinding.today.setOnClickListener(this)
        setCooker(offset = 0, reset = true, tillToday = true)
        batteryBinding.batteryListView.setOnItemClickListener { parent, _, position, _ ->
            redirectToAppInfo(parent, position)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.leftArrow -> setCooker(offset = -1)
            R.id.rightArrow -> setCooker(offset = 1)
            R.id.description -> toggleCookingMode(v as TextView)
            R.id.today -> selectDate()
        }
    }

    private fun selectDate() {
        DatePickerDialog(
            this,
            datePickerListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private var datePickerListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            setCooker()
        }

    private fun setCooker(offset: Int = 0, reset: Boolean = false, tillToday: Boolean = false) {
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

    private fun toggleCookingMode(v: TextView) {
        if (v.text == getString(R.string.till_today)) {
            v.text = getString(R.string.day_wise)
            batteryBinding.batteryDatePicker.isVisible = true
            setCooker(offset = 0, reset = true)
        } else {
            v.text = getString(R.string.till_today)
            batteryBinding.batteryDatePicker.isVisible = false
            setCooker(offset = 0, reset = true, tillToday = true)
        }
    }

    private val onCookingDone = object : ICookingDone {
        override fun onEmptyDB() {
            batteryBinding.root.post {
                batteryBinding.batteryListView.adapter =
                    BatteryListAdapter(this@BatteryActivity, R.layout.battery_tile, arrayListOf())
                batteryBinding.total.text = getString(R.string.no_usage_recorded)
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onData(appEntryList: ArrayList<AppEntry>, totalDrop: Int) {
            batteryBinding.root.post {
                batteryBinding.batteryListView.adapter =
                    BatteryListAdapter(this@BatteryActivity, R.layout.battery_tile, appEntryList)
                batteryBinding.total.text = "Total drop is $totalDrop %"
            }
        }
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

interface ICookingDone {
    fun onEmptyDB()
    fun onData(appEntryList: ArrayList<AppEntry>, totalDrop: Int)
}


package com.example.androidDeviceDetails

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.androidDeviceDetails.adapters.BatteryListAdapter
import com.example.androidDeviceDetails.managers.AppBatteryUsageManager
import com.example.androidDeviceDetails.utils.Utils
import java.util.*


class BatteryActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var calendar: Calendar
    private lateinit var totalTextView: TextView
    private lateinit var todayTextView: TextView
    private lateinit var batteryListView: ListView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battery)
        batteryListView = findViewById(R.id.batteryListView)
        totalTextView = findViewById(R.id.total)
        todayTextView = findViewById(R.id.today)

        val previousDayButton = findViewById<ImageButton>(R.id.leftArrow)
        val nextDayButton = findViewById<ImageButton>(R.id.rightArrow)
        val timePickerLayout = findViewById<LinearLayout>(R.id.battery_date_picker)
        timePickerLayout.isVisible = false
        previousDayButton.setOnClickListener(this)
        nextDayButton.setOnClickListener(this)
        calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
//        val items = arrayListOf<AppEntry>()
//        items.add(AppEntry("com.whatsapp", 6))
//        items.add(AppEntry("com.example.Broadband", 4))
//        items.add(AppEntry("com.android.packageinstaller", 4))
//        items.add(AppEntry("com.microsoft.teams", 4))
//        items.add(AppEntry("com.google.android.apps.photos", 4))
//        batteryListView.adapter = BatteryListAdapter(this, R.layout.battery_tile, items)

        AppBatteryUsageManager().cookBatteryData(
            this,
            batteryListView,
            totalTextView,
            calendar.timeInMillis,
            calendar.timeInMillis + 24 * 60 * 60 * 1000
        )
        batteryListView.setOnItemClickListener { parent, _, position, _ ->
            val adapter = parent.adapter as BatteryListAdapter
            val item = adapter.getItem(position)
            Log.d("TAG", "onCreate: $position")
            val infoIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            infoIntent.addCategory(Intent.CATEGORY_DEFAULT)
            infoIntent.data = Uri.parse("package:${item?.packageId}")
            startActivity(infoIntent)
        }

        findViewById<TextView>(R.id.description).setOnClickListener {
            it as TextView
            if (it.text == "Till today") {
                it.text = "Day wise"
                timePickerLayout.isVisible = true
            } else {
                it.text = "Till today"
                timePickerLayout.isVisible = false
            }
            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            todayTextView.text =
                "${Utils.getWeek(calendar.get(Calendar.DAY_OF_WEEK))}, ${calendar.get(Calendar.DAY_OF_MONTH)} ${
                    Utils.getMonth(calendar.get(Calendar.MONTH))
                }"
            AppBatteryUsageManager().cookBatteryData(
                this,
                batteryListView,
                totalTextView,
                calendar.timeInMillis,
                calendar.timeInMillis + 24 * 60 * 60 * 1000
            )
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.leftArrow -> {
                Log.d("TAG", "onClick: left")
                calendar.add(Calendar.DAY_OF_MONTH, -1)
                todayTextView.text =
                    "${Utils.getWeek(calendar.get(Calendar.DAY_OF_WEEK))}, ${calendar.get(Calendar.DAY_OF_MONTH)} ${
                        Utils.getMonth(calendar.get(Calendar.MONTH))
                    }"
                AppBatteryUsageManager().cookBatteryData(
                    this,
                    batteryListView,
                    totalTextView,
                    calendar.timeInMillis,
                    calendar.timeInMillis + 24 * 60 * 60 * 1000
                )

            }
            R.id.rightArrow -> {
                Log.d("TAG", "onClick: right")
                calendar.add(Calendar.DAY_OF_MONTH, 1)
                todayTextView.text =
                    "${Utils.getWeek(calendar.get(Calendar.DAY_OF_WEEK))}, ${calendar.get(Calendar.DAY_OF_MONTH)} ${
                        Utils.getMonth(calendar.get(Calendar.MONTH))
                    }"
                AppBatteryUsageManager().cookBatteryData(
                    this,
                    batteryListView,
                    totalTextView,
                    calendar.timeInMillis,
                    calendar.timeInMillis + 24 * 60 * 60 * 1000
                )
            }
        }
    }
}


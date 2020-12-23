package com.example.androidDeviceDetails.battery

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.androidDeviceDetails.R
import com.example.androidDeviceDetails.databinding.ActivityBatteryBinding
import java.util.*

class BatteryActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var calendar: Calendar
    private lateinit var batteryBinding: ActivityBatteryBinding
    private lateinit var batteryController: BatteryController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        batteryBinding = DataBindingUtil.setContentView(this, R.layout.activity_battery)
        batteryController = BatteryController(this, batteryBinding)
        batteryBinding.apply {
            leftArrow.setOnClickListener(this@BatteryActivity)
            rightArrow.setOnClickListener(this@BatteryActivity)
            description.setOnClickListener(this@BatteryActivity)
            today.setOnClickListener(this@BatteryActivity)
            batteryListView.setOnItemClickListener { parent, _, position, _ ->
                batteryController.redirectToAppInfo(parent, position)
            }
        }
        batteryController.setCooker(offset = 0, reset = true, tillToday = true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.leftArrow -> batteryController.setCooker(offset = -1)
            R.id.rightArrow -> batteryController.setCooker(offset = 1)
            R.id.description -> batteryController.toggleCookingMode(v as TextView)
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
            batteryController.setCooker()
        }
}


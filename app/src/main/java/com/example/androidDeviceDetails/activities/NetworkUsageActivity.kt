package com.example.androidDeviceDetails.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.androidDeviceDetails.R
import com.example.androidDeviceDetails.controller.NetworkUsageController
import com.example.androidDeviceDetails.databinding.ActivityAppDataBinding
import java.util.*

class NetworkUsageActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var networkUsageBinding: ActivityAppDataBinding
    private lateinit var networkUsageController: NetworkUsageController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkUsageBinding = DataBindingUtil.setContentView(this, R.layout.activity_app_data)
        networkUsageController = NetworkUsageController(this, networkUsageBinding)
        networkUsageBinding.apply {
            startTime.setOnClickListener(this@NetworkUsageActivity)
            startDate.setOnClickListener(this@NetworkUsageActivity)
            endTime.setOnClickListener(this@NetworkUsageActivity)
            endDate.setOnClickListener(this@NetworkUsageActivity)
        }
        networkUsageController.startCalendar.add(Calendar.DAY_OF_MONTH, -1)
        networkUsageController.setCooker()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.startTime -> setStartTime()
            R.id.startDate -> setStartDate()
            R.id.endTime -> setEndTime()
            R.id.endDate -> setEndDate()
        }
    }

    private fun setStartTime() {
        val hour = networkUsageController.startCalendar.get(Calendar.HOUR)
        val minute = networkUsageController.startCalendar.get(Calendar.MINUTE)
        TimePickerDialog(
            this@NetworkUsageActivity, startTimePickerListener, hour, minute,
            DateFormat.is24HourFormat(this)
        ).show()
    }

    private val startTimePickerListener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            networkUsageController.startCalendar[Calendar.HOUR_OF_DAY] = hourOfDay
            networkUsageController.startCalendar[Calendar.MINUTE] = minute
            networkUsageController.setCooker()
        }

    private fun setStartDate() {
        val day = networkUsageController.startCalendar.get(Calendar.DAY_OF_MONTH)
        val month = networkUsageController.startCalendar.get(Calendar.MONTH)
        val year = networkUsageController.startCalendar.get(Calendar.YEAR)
        DatePickerDialog(
            this@NetworkUsageActivity,
            startDatePickerListener,
            year,
            month,
            day
        ).show()
    }

    private var startDatePickerListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            networkUsageController.startCalendar.set(year, month, dayOfMonth)
            networkUsageController.setCooker()
        }

    private fun setEndTime() {
        val hour = networkUsageController.startCalendar.get(Calendar.HOUR)
        val minute = networkUsageController.startCalendar.get(Calendar.MINUTE)
        TimePickerDialog(
            this@NetworkUsageActivity, endTimePickerListener, hour, minute,
            DateFormat.is24HourFormat(this)
        ).show()
    }

    private val endTimePickerListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        networkUsageController.endCalendar[Calendar.HOUR_OF_DAY] = hourOfDay
        networkUsageController.endCalendar[Calendar.MINUTE] = minute
        networkUsageController.setCooker()
    }

    private fun setEndDate() {
        val day = networkUsageController.endCalendar.get(Calendar.DAY_OF_MONTH)
        val month = networkUsageController.endCalendar.get(Calendar.MONTH)
        val year = networkUsageController.endCalendar.get(Calendar.YEAR)
        DatePickerDialog(
            this@NetworkUsageActivity,
            endDatePickerListener,
            year,
            month,
            day
        ).show()
    }

    private var endDatePickerListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            networkUsageController.endCalendar.set(year, month, dayOfMonth)
            networkUsageController.setCooker()
        }


}
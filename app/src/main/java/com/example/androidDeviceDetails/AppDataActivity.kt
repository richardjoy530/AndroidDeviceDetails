package com.example.androidDeviceDetails

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.androidDeviceDetails.databinding.ActivityAppDataBinding
import java.text.SimpleDateFormat
import java.util.*

class AppDataActivity : AppCompatActivity(), View.OnClickListener  {
    private val startCalendar: Calendar = Calendar.getInstance()
    private lateinit var binding: ActivityAppDataBinding
    private val simpleDateFormat = SimpleDateFormat("HH:mm',' dd/MM/yyyy")

    private var startTime: Long = 0
    private var endTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_app_data)
        binding.startDateViewAppData.setOnClickListener(this)
        binding.endDateViewAppData.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.startDateViewAppData -> setStartDate()
            R.id.endDateViewAppData -> setEndDate()
        }

    }

    private fun setStartDate() {
        val day = startCalendar.get(Calendar.DAY_OF_MONTH)
        val month = startCalendar.get(Calendar.MONTH)
        val year = startCalendar.get(Calendar.YEAR)
        DatePickerDialog(
            this@AppDataActivity,
            startDatePickerListener,
            year,
            month,
            day
        ).show()
    }
    private var startDatePickerListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            startCalendar.set(year, month, dayOfMonth)
            setStartTime()
        }
    private  fun setStartTime(){
        val hour = startCalendar.get(Calendar.HOUR)
        val minute = startCalendar.get(Calendar.MINUTE)
        TimePickerDialog(
            this@AppDataActivity, startTimePickerListener, hour, minute,
            DateFormat.is24HourFormat(this)
        ).show()
    }


    private val startTimePickerListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        startCalendar[Calendar.HOUR_OF_DAY] = hourOfDay
        startCalendar[Calendar.MINUTE] = minute
        startTime=startCalendar.timeInMillis
        Log.d("TAG", "StartTime: $startTime")


    }

    private fun setEndDate() {
        val day = startCalendar.get(Calendar.DAY_OF_MONTH)
        val month = startCalendar.get(Calendar.MONTH)
        val year = startCalendar.get(Calendar.YEAR)
        DatePickerDialog(
            this@AppDataActivity,
            endDatePickerListener,
            year,
            month,
            day
        ).show()
    }
    private var endDatePickerListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            startCalendar.set(year, month, dayOfMonth)
            setEndTime()
        }
    private  fun setEndTime(){
        val hour = startCalendar.get(Calendar.HOUR)
        val minute = startCalendar.get(Calendar.MINUTE)
        TimePickerDialog(
            this@AppDataActivity, endTimePickerListener, hour, minute,
            DateFormat.is24HourFormat(this)
        ).show()
    }


    private val endTimePickerListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        startCalendar[Calendar.HOUR_OF_DAY] = hourOfDay
        startCalendar[Calendar.MINUTE] = minute
        endTime=startCalendar.timeInMillis
        Log.d("TAG", "EndTime: $endTime")

    }

}
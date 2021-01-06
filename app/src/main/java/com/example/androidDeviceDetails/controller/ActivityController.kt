package com.example.androidDeviceDetails.controller

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.format.DateFormat
import com.example.androidDeviceDetails.ICookingDone
import com.example.androidDeviceDetails.base.BaseCooker
import com.example.androidDeviceDetails.base.BaseViewModel
import com.example.androidDeviceDetails.models.TimeInterval
import java.util.*

class ActivityController<T, MT>(dataType: String, binding: T, val context: Context) {

    private var cooker: BaseCooker = BaseCooker.getCooker(dataType)
    private var viewModel: BaseViewModel = BaseViewModel.getViewModel(dataType, binding, context)
    private var startCalendar: Calendar = Calendar.getInstance()
    private var endCalendar: Calendar=Calendar.getInstance()
    init {
        startCalendar.set(Calendar.HOUR, 0)
        startCalendar.set(Calendar.MINUTE, 0)
    }


    fun cook(timeInterval: TimeInterval) {
        cooker.cook(timeInterval, onCookingDone)
    }

    private val onCookingDone = object : ICookingDone<MT> {
        override fun onDone(outputList: ArrayList<MT>) =
            viewModel.onData(outputList)
    }

    fun setStartTime(context: Context) {
        val hour = startCalendar.get(Calendar.HOUR)
        val minute = startCalendar.get(Calendar.MINUTE)
        TimePickerDialog(
            context, startTimePickerListener, hour, minute,
            DateFormat.is24HourFormat(context)
        ).show()
    }
    private val startTimePickerListener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            startCalendar[Calendar.HOUR_OF_DAY] = hourOfDay
            startCalendar[Calendar.MINUTE] = minute
            cook(
                TimeInterval(
                    startCalendar.timeInMillis,
                    endCalendar.timeInMillis
                )
            )
        }
    fun setStartDate(context: Context) {
        val day = startCalendar.get(Calendar.DAY_OF_MONTH)
        val month = startCalendar.get(Calendar.MONTH)
        val year = startCalendar.get(Calendar.YEAR)
        DatePickerDialog(
            context,
            startDatePickerListener,
            year,
            month,
            day
        ).show()
    }
    private var startDatePickerListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            startCalendar.set(year, month, dayOfMonth)
            cook(
                TimeInterval(
                    startCalendar.timeInMillis,
                    endCalendar.timeInMillis
                )
            )
        }
    fun setEndTime(context: Context) {
        val hour = startCalendar.get(Calendar.HOUR)
        val minute = startCalendar.get(Calendar.MINUTE)
        TimePickerDialog(
            context, endTimePickerListener, hour, minute,
            DateFormat.is24HourFormat(context)
        ).show()
    }

    private val endTimePickerListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        endCalendar[Calendar.HOUR_OF_DAY] = hourOfDay
        endCalendar[Calendar.MINUTE] = minute
        cook(
            TimeInterval(
                startCalendar.timeInMillis,
                endCalendar.timeInMillis
            )
        )
    }

    fun setEndDate(context: Context) {
        val day = endCalendar.get(Calendar.DAY_OF_MONTH)
        val month = endCalendar.get(Calendar.MONTH)
        val year = endCalendar.get(Calendar.YEAR)
        DatePickerDialog(
            context,
            endDatePickerListener,
            year,
            month,
            day
        ).show()
    }

    private var endDatePickerListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            endCalendar.set(year, month, dayOfMonth)
          cook(
                TimeInterval(
                    startCalendar.timeInMillis,
                    endCalendar.timeInMillis
                )
            )
        }

}


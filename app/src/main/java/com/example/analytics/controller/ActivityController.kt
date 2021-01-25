package com.example.analytics.controller

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.example.analytics.R
import com.example.analytics.base.BaseCooker
import com.example.analytics.base.BaseViewModel
import com.example.analytics.databinding.DateTimePickerBinding
import com.example.analytics.fragments.BottomSheet
import com.example.analytics.interfaces.ICookingDone
import com.example.analytics.models.TimePeriod
import com.example.analytics.utils.Utils
import java.util.*
import java.util.concurrent.TimeUnit

class ActivityController<T>(
    dataType: String, binding: ViewBinding, var context: Context,
    private val pickerBinding: DateTimePickerBinding?,
    private val supportFragmentManager: FragmentManager
) {

    private var cooker: BaseCooker? = BaseCooker.getCooker(dataType)
    var viewModel: BaseViewModel? = BaseViewModel.getViewModel(dataType, binding, context)
    private var startCalendar: Calendar = Calendar.getInstance()
    private var endCalendar: Calendar = Calendar.getInstance()
    private var previousStartTime: Long = 0
    private var previousEndTime: Long = 0
    private var isStartCalendar = true


    private val onCookingDone = object : ICookingDone<T> {
        override fun onDone(outputList: ArrayList<T>) {

            if (pickerBinding != null) {
                viewModel?.isLoading(pickerBinding, false)
            }
            viewModel?.onDone(outputList)
        }
    }

    init {
        refreshCooker()
    }

    fun refreshCooker() {
        startCalendar.set(Calendar.HOUR_OF_DAY, 0)
        startCalendar.set(Calendar.MINUTE, 0)
        previousStartTime = startCalendar.timeInMillis
        previousEndTime = endCalendar.timeInMillis
        showInitialData()
        cook(TimePeriod(startCalendar.timeInMillis, endCalendar.timeInMillis))
    }

    private fun cook(timePeriod: TimePeriod) {
        if (pickerBinding != null)
            viewModel?.isLoading(pickerBinding, true)
        cooker?.cook(timePeriod, onCookingDone)
    }


    fun setTime(context: Context, id: Int) {
        isStartCalendar = id == R.id.startTime
        val hour = startCalendar.get(Calendar.HOUR)
        val minute = startCalendar.get(Calendar.MINUTE)
        TimePickerDialog(context, timePickerListener, hour, minute, false).show()
    }

    private val timePickerListener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            if (isStartCalendar) {
                startCalendar[Calendar.HOUR_OF_DAY] = hourOfDay
                startCalendar[Calendar.MINUTE] = minute
                if (previousStartTime != startCalendar.timeInMillis)
                    validateTimeInterval()
                previousStartTime = startCalendar.timeInMillis
            } else {
                endCalendar[Calendar.HOUR_OF_DAY] = hourOfDay
                endCalendar[Calendar.MINUTE] = minute
                if (previousEndTime != endCalendar.timeInMillis)
                    validateTimeInterval()
                previousEndTime = endCalendar.timeInMillis
            }

        }

    fun setDate(context: Context, id: Int) {
        isStartCalendar = id == R.id.startDate
        val day = startCalendar.get(Calendar.DAY_OF_MONTH)
        val month = startCalendar.get(Calendar.MONTH)
        val year = startCalendar.get(Calendar.YEAR)
        val datePicker = DatePickerDialog(context, datePickerListener, year, month, day)
        if (isStartCalendar)
            datePicker.datePicker.maxDate = endCalendar.timeInMillis
        else {
            datePicker.datePicker.minDate = startCalendar.timeInMillis
            datePicker.datePicker.maxDate = System.currentTimeMillis()
        }
        datePicker.show()
    }

    private var datePickerListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            if (isStartCalendar) {
                startCalendar.set(year, month, dayOfMonth)
                if (previousStartTime != startCalendar.timeInMillis)
                    validateTimeInterval()
                previousStartTime = startCalendar.timeInMillis
            } else {
                endCalendar.set(year, month, dayOfMonth)
                if (previousEndTime != endCalendar.timeInMillis)
                    validateTimeInterval()
                previousEndTime = endCalendar.timeInMillis
            }
        }

    private fun validateTimeInterval() {
        if (startCalendar.timeInMillis < endCalendar.timeInMillis)
            if ((endCalendar.timeInMillis - startCalendar.timeInMillis) > TimeUnit.MINUTES.toMillis(
                    Utils.COLLECTION_INTERVAL
                )
            ) {
                BottomSheet(onApply = { onClickApply() }).show(supportFragmentManager, "Apply")
                if (pickerBinding != null) {
                    viewModel?.updateDateTimeUI(startCalendar, endCalendar, pickerBinding)
                }
            } else
                Toast.makeText(
                    context,
                    "Time interval should be greater than ${Utils.COLLECTION_INTERVAL} minutes",
                    Toast.LENGTH_SHORT
                ).show()
        else Toast.makeText(context, "Enter valid time interval", Toast.LENGTH_SHORT).show()
    }

    private fun onClickApply() {
        startCalendar.set(Calendar.SECOND, 0)
        endCalendar.set(Calendar.SECOND, 0)
        startCalendar.set(Calendar.MILLISECOND, 0)
        endCalendar.set(Calendar.MILLISECOND, 0)
        cook(TimePeriod(startCalendar.timeInMillis, endCalendar.timeInMillis))
    }

    private fun showInitialData() {
        if (pickerBinding != null)
            viewModel?.updateDateTimeUI(startCalendar, endCalendar, pickerBinding)
    }

    fun filterView(type: Int) {
        viewModel?.filter(type)
    }

    fun sortView(type: Int) {
        viewModel?.sort(type)
    }
}


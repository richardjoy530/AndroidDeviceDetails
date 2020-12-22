package com.example.androidDeviceDetails

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.androidDeviceDetails.adapters.AppDataListAdapter
import com.example.androidDeviceDetails.databinding.ActivityAppDataBinding
import com.example.androidDeviceDetails.models.AppDataUsage
import com.example.androidDeviceDetails.models.RoomDB
import com.example.androidDeviceDetails.utils.Utils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.util.*

class AppDataActivity : AppCompatActivity(), View.OnClickListener {
    private val startCalendar: Calendar = Calendar.getInstance()
    private val endCalendar: Calendar = Calendar.getInstance()
    private lateinit var binding: ActivityAppDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_app_data)
        binding.apply {
            startTime.setOnClickListener(this@AppDataActivity)
            startDate.setOnClickListener(this@AppDataActivity)
            endTime.setOnClickListener(this@AppDataActivity)
            endDate.setOnClickListener(this@AppDataActivity)
        }
        startCalendar.add(Calendar.DAY_OF_MONTH, -1)
        updateTextViews()
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
        val hour = startCalendar.get(Calendar.HOUR)
        val minute = startCalendar.get(Calendar.MINUTE)
        TimePickerDialog(
            this@AppDataActivity, startTimePickerListener, hour, minute,
            DateFormat.is24HourFormat(this)
        ).show()
    }

    private val startTimePickerListener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            startCalendar[Calendar.HOUR_OF_DAY] = hourOfDay
            startCalendar[Calendar.MINUTE] = minute
            updateTextViews()
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
            updateTextViews()
        }

    private fun setEndTime() {
        val hour = startCalendar.get(Calendar.HOUR)
        val minute = startCalendar.get(Calendar.MINUTE)
        TimePickerDialog(
            this@AppDataActivity, endTimePickerListener, hour, minute,
            DateFormat.is24HourFormat(this)
        ).show()
    }

    private val endTimePickerListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        endCalendar[Calendar.HOUR_OF_DAY] = hourOfDay
        endCalendar[Calendar.MINUTE] = minute
        updateTextViews()
    }

    private fun setEndDate() {
        val day = endCalendar.get(Calendar.DAY_OF_MONTH)
        val month = endCalendar.get(Calendar.MONTH)
        val year = endCalendar.get(Calendar.YEAR)
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
            endCalendar.set(year, month, dayOfMonth)
            updateTextViews()
        }

    private fun updateTextViews() {
        val dec = DecimalFormat("00")

        var startTime = dec.format(startCalendar.get(Calendar.HOUR)) + ":"
        startTime += dec.format(startCalendar.get(Calendar.MINUTE))

        var endTime = dec.format(endCalendar.get(Calendar.HOUR)) + ":"
        endTime += dec.format(endCalendar.get(Calendar.MINUTE))

        var startDate = startCalendar.get(Calendar.DAY_OF_MONTH).toString() + ", "
        startDate += Utils.getMonth(startCalendar.get(Calendar.MONTH)) + " "
        startDate += startCalendar.get(Calendar.YEAR)

        var endDate = endCalendar.get(Calendar.DAY_OF_MONTH).toString() + ", "
        endDate += Utils.getMonth(endCalendar.get(Calendar.MONTH)) + " "
        endDate += endCalendar.get(Calendar.YEAR)

        binding.apply {
            this.startTime.text = startTime
            this.startDate.text = startDate
            this.endTime.text = endTime
            this.endDate.text = endDate
            this.startAMPM.text = if (startCalendar.get(Calendar.AM_PM) == 0) "am" else "pm"
        }
        startCalendar.set(Calendar.SECOND, 0)
        endCalendar.set(Calendar.SECOND, 0)
        startCalendar.set(Calendar.MILLISECOND, 0)
        endCalendar.set(Calendar.MILLISECOND, 0)
        appDataCooker(startCalendar.timeInMillis, endCalendar.timeInMillis)
    }

    private fun appDataCooker(startTime: Long, endTime: Long) {
        val db = RoomDB.getDatabase()?.appDataUsage()!!
        GlobalScope.launch {
            val inBetweenList = db.getAllBetween(startTime, endTime)
            val firstElementTime = inBetweenList.first().timeStamp
            val initialAppDataList = inBetweenList.filter { it.timeStamp == firstElementTime }
            val lastElementTime = inBetweenList.last().timeStamp
            val finalAppDataList = inBetweenList.filter { it.timeStamp == lastElementTime }
            val totalDataUsageList = arrayListOf<AppDataUsage>()
            finalAppDataList.forEach {
                val nullCheckList =
                    initialAppDataList.filter { appDataUsage -> it.packageName == appDataUsage.packageName }
                if (nullCheckList.isNotEmpty()) {
                    val initialAppData = nullCheckList[0]
                    totalDataUsageList.add(
                        AppDataUsage(
                            0,
                            it.timeStamp,
                            it.packageName,
                            it.transferredDataWifi - initialAppData.transferredDataWifi,
                            it.transferredDataMobile - initialAppData.transferredDataMobile,
                            it.receivedDataWifi - initialAppData.receivedDataWifi,
                            it.receivedDataMobile - initialAppData.receivedDataMobile
                        )
                    )
                } else totalDataUsageList.add(it)
            }
            binding.root.post {
                binding.appDataListView.adapter = AppDataListAdapter(
                    this@AppDataActivity,
                    R.layout.appdata_tile,
                    totalDataUsageList
                )
            }
        }

    }

}
package com.example.androidDeviceDetails.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.androidDeviceDetails.R
import com.example.androidDeviceDetails.controller.ActivityController
import com.example.androidDeviceDetails.databinding.ActivityAppDataBinding
import com.example.androidDeviceDetails.models.TimeInterval
import com.example.androidDeviceDetails.models.networkUsageModels.AppNetworkUsageEntity
import java.util.*

class NetworkUsageActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var networkUsageBinding: ActivityAppDataBinding
    private lateinit var networkUsageController: ActivityController<ActivityAppDataBinding, AppNetworkUsageEntity>
    private lateinit var startCalendar: Calendar
    private lateinit var endCalendar: Calendar


    companion object {
        const val NAME = "network"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkUsageBinding = DataBindingUtil.setContentView(this, R.layout.activity_app_data)
        networkUsageController = ActivityController(NAME, networkUsageBinding, this)
        startCalendar = Calendar.getInstance()
        startCalendar.set(Calendar.HOUR, 0)
        startCalendar.set(Calendar.MINUTE, 0)
        endCalendar = Calendar.getInstance()
        networkUsageBinding.apply {
            startTime.setOnClickListener(this@NetworkUsageActivity)
            startDate.setOnClickListener(this@NetworkUsageActivity)
            endTime.setOnClickListener(this@NetworkUsageActivity)
            endDate.setOnClickListener(this@NetworkUsageActivity)
        }
        startCalendar.add(Calendar.DAY_OF_MONTH, -1)
        networkUsageController.cook(
            TimeInterval(
                startCalendar.timeInMillis,
                endCalendar.timeInMillis
            )
        )
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.startTime -> networkUsageController.setStartTime(this)
            R.id.startDate -> networkUsageController.setStartDate(this)
            R.id.endTime -> networkUsageController.setEndTime(this)
            R.id.endDate -> networkUsageController.setEndDate(this)
        }
    }

}
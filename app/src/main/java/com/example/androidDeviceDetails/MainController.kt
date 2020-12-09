package com.example.androidDeviceDetails

import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.androidDeviceDetails.managers.AppBatteryUsageManager
import com.example.androidDeviceDetails.services.TestService

class MainController {

    fun toggleService(context: Context, enable: Boolean) {
        if (enable)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context.startForegroundService(Intent(context, TestService::class.java))
            else context.startService(Intent(context, TestService::class.java))
        else context.stopService(Intent(context, TestService::class.java))
    }

    fun getAppBatteryUsage(beginTime: Long, endTime: Long) {
        val appBatteryUsageManager = AppBatteryUsageManager()
        appBatteryUsageManager.cookBatteryData(beginTime, endTime)
    }
}
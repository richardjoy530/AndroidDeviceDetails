package com.example.analytics.controller

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.analytics.base.BaseCollector
import com.example.analytics.collectors.AppEventCollector
import com.example.analytics.collectors.AppInfoCollector
import com.example.analytics.collectors.BatteryCollector
import com.example.analytics.collectors.NetworkUsageCollector
import java.util.*

class ApplicationController(context: Context) {
    lateinit var timer: Timer

    @RequiresApi(Build.VERSION_CODES.M)
    var instanceMap: MutableMap<String, BaseCollector> = mutableMapOf(
        "BatteryReceiver" to BatteryCollector(context),
//        "WifiReceiver" to WifiCollector(),
        "AppStateReceiver" to AppInfoCollector(context),
        "AppEventCollector" to AppEventCollector(context),
//        "SignalChangeListener" to SignalChangeCollector(),
        "NetworkUsageCollector" to NetworkUsageCollector(context),
    )

    fun runTimer(intervalInMinuets: Long) {
        timer = Timer()
        timer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    for (collector in instanceMap.values) {
                        collector.collect()
                    }
                }
            },
            0, 1000 * 60 * intervalInMinuets
        )
    }

}
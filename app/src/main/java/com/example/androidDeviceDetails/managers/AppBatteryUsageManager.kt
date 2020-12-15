package com.example.androidDeviceDetails.managers

import android.annotation.SuppressLint
import android.content.Context
import com.example.androidDeviceDetails.R
import com.example.androidDeviceDetails.adapters.BatteryListAdapter
import com.example.androidDeviceDetails.databinding.ActivityBatteryBinding
import com.example.androidDeviceDetails.models.RoomDB
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AppBatteryUsageManager {
    private var db: RoomDB = RoomDB.getDatabase()!!

    @SuppressLint("SetTextI18n")
    fun cookBatteryData(
        context: Context,
        batteryBinding: ActivityBatteryBinding,
        beginTime: Long,
        endTime: Long = System.currentTimeMillis()
    ) {
        val appEntryList = arrayListOf<AppEntry>()
        GlobalScope.launch {
            val appEventList = db.appUsageInfoDao().getAllBetween(beginTime, endTime)
            val batteryList = db.batteryInfoDao().getAllBetween(beginTime, endTime)
            if (batteryList.isNotEmpty() && appEventList.isNotEmpty()) {
                    val batteryIterator = batteryList.iterator()
                    var batteryInfo = batteryList.first()
                    var previousBattery = batteryList.first()
                    var previousApp = appEventList.first()
                    for (appEvent in appEventList) {
                        while ((appEvent.timeStamp > batteryInfo.timeStamp || batteryInfo.health == 0) && batteryIterator.hasNext())
                            batteryInfo = batteryIterator.next()
                        if (batteryInfo.plugged == 0 && previousBattery.level!! > batteryInfo.level!!)
                            if (appEntryList.none { it.packageId == previousApp.packageName })
                                appEntryList.add(
                                    AppEntry(
                                        previousApp.packageName,
                                        previousBattery.level!!.minus(batteryInfo.level!!)
                                    )
                                )
                            else appEntryList.first { it.packageId == previousApp.packageName }.drop +=
                                previousBattery.level!!.minus(batteryInfo.level!!)
                        previousApp = appEvent
                        previousBattery = batteryInfo
                    }
                var totalDrop = 0
                for (i in appEntryList) totalDrop += i.drop
                batteryBinding.batteryListView.post {
                    batteryBinding.batteryListView.adapter =
                        BatteryListAdapter(context, R.layout.battery_tile, appEntryList)
                }
                batteryBinding.total.post {
                    batteryBinding.total.text = "Total drop is $totalDrop %"
                }
            } else {
                batteryBinding.batteryListView.post {
                    batteryBinding.batteryListView.adapter =
                        BatteryListAdapter(context, R.layout.battery_tile, arrayListOf())
                }
                batteryBinding.total.post { batteryBinding.total.text = "No usage recorded" }
            }
        }
    }
}

data class AppEntry(var packageId: String, var drop: Int = 0)
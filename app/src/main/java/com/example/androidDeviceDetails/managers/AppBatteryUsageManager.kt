package com.example.androidDeviceDetails.managers

import com.example.androidDeviceDetails.ICookingDone
import com.example.androidDeviceDetails.models.RoomDB
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AppBatteryUsageManager {
    private var db: RoomDB = RoomDB.getDatabase()!!

    fun cookBatteryData(
        callback: ICookingDone,
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
                callback.onData(appEntryList, totalDrop)
            } else callback.onNoData()
        }
    }
}

data class AppEntry(var packageId: String, var drop: Int = 0)
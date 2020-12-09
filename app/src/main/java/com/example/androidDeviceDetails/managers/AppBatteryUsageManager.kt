package com.example.androidDeviceDetails.managers

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.androidDeviceDetails.models.AppUsageRaw
import com.example.androidDeviceDetails.models.BatteryRaw
import com.example.androidDeviceDetails.models.RoomDB
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AppBatteryUsageManager {
    private var db: RoomDB = RoomDB.getDatabase()!!

    private fun getCombinedList(
        appEventList: List<AppUsageRaw>,
        batteryList: List<BatteryRaw>
    ): MutableList<MergedEventData> {

        val mergedList = mutableListOf<MergedEventData>()
        val batteryIterator = batteryList.iterator()
        var preBattery = batteryList.first()

        for (it in appEventList) {
            while (it.timeStamp > preBattery.timeStamp && batteryIterator.hasNext())
                preBattery = batteryIterator.next()
            mergedList.add(
                MergedEventData(
                    it.timeStamp,
                    preBattery.level,
                    preBattery.plugged,
                    it.packageName,
                )
            )
        }
        return mergedList
    }

    fun cookBatteryData(
        beginTime: Long,
        endTime: Long = System.currentTimeMillis()
    ) {
        val appEntryList = arrayListOf<AppEntry>()
        GlobalScope.launch {
            val appEventList = db.appUsageInfoDao().getAllBetween(beginTime, endTime)
            val batteryList = db.batteryInfoDao().getAllBetween(beginTime, endTime)

            val mergedList = getCombinedList(appEventList, batteryList)
            var previousData = mergedList.first()
            for ((i, mergedEventData) in mergedList.withIndex()) {
                if (mergedEventData.plugged == 0 && previousData.batteryLevel!! > mergedEventData.batteryLevel!!)
                    if (appEntryList.none { it.packageId == mergedEventData.packageName })
                        appEntryList.add(
                            AppEntry(
                                mergedEventData.packageName,
                                previousData.batteryLevel!!.minus(mergedEventData.batteryLevel!!)
                            )
                        )
                    else appEntryList.first { it.packageId == mergedList[i - 1].packageName }.drop +=
                        previousData.batteryLevel!!.minus(mergedEventData.batteryLevel!!)
                previousData = mergedEventData
            }
        }
    }
}

data class MergedEventData(
    var timestamp: Long,
    var batteryLevel: Int?,
    var plugged: Int?,
    var packageName: String,
)

data class AppEntry(var packageId: String, var drop: Int = 0)
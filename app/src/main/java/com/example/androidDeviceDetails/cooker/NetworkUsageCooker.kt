package com.example.androidDeviceDetails.cooker

import com.example.androidDeviceDetails.ICookingDone
import com.example.androidDeviceDetails.models.RoomDB
import com.example.androidDeviceDetails.models.networkUsageModels.AppNetworkUsageEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NetworkUsageCooker {
    fun appDataCooker(
        callback: ICookingDone<AppNetworkUsageEntity>,
        startTime: Long,
        endTime: Long
    ) {
        val db = RoomDB.getDatabase()?.appNetworkUsageDao()!!
        GlobalScope.launch {
            val inBetweenList = db.getAllBetween(startTime, endTime)
            if (inBetweenList.isNotEmpty()) {
                val firstElementTime = inBetweenList.first().timeStamp
                val initialAppDataList = inBetweenList.filter { it.timeStamp == firstElementTime }
                val lastElementTime = inBetweenList.last().timeStamp
                val finalAppDataList = inBetweenList.filter { it.timeStamp == lastElementTime }
                val totalDataUsageList = arrayListOf<AppNetworkUsageEntity>()
                finalAppDataList.forEach {
                    val nullCheckList =
                        initialAppDataList.filter { appDataUsage -> it.packageName == appDataUsage.packageName }
                    if (nullCheckList.isNotEmpty()) {
                        val initialAppData = nullCheckList[0]
                        totalDataUsageList.add(
                            AppNetworkUsageEntity(
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
                callback.onData(totalDataUsageList)
            } else callback.onNoData()

        }
    }

}
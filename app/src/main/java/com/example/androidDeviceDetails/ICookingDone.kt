package com.example.androidDeviceDetails

import com.example.androidDeviceDetails.models.batteryModels.BatteryAppEntry
import java.util.*

interface ICookingDone {
    fun onNoData()
    fun onData(batteryAppEntryList: ArrayList<BatteryAppEntry>, totalDrop: Int)
}
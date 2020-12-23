package com.example.androidDeviceDetails

import com.example.androidDeviceDetails.managers.AppEntry
import java.util.*

interface ICookingDone {
    fun onNoData()
    fun onData(appEntryList: ArrayList<AppEntry>, totalDrop: Int)
}
package com.example.androidDeviceDetails

import android.util.Log
import com.example.androidDeviceDetails.models.RoomDB
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Test {
    private var signalDB = RoomDB.getDatabase()!!
    fun log() {
        GlobalScope.launch {
            for (info in signalDB.wifiDao().getAll())
                Log.d("wifi", "$info")
        }
    }

    fun log1() {
        GlobalScope.launch {
            Log.d("cellular", "inside cellular")
            for (info in signalDB.cellularDao().getAll())
                Log.d("cellular", "$info")
        }
    }
}
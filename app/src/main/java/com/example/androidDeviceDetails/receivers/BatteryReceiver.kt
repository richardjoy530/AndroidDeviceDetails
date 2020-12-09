package com.example.androidDeviceDetails.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.util.Log
import com.example.androidDeviceDetails.models.BatteryRawModel
import com.example.androidDeviceDetails.models.RoomDB
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BatteryReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val batteryManager: BatteryManager =
            context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val db = RoomDB.getDatabase(context)
        val batteryRaw = BatteryRawModel(
            System.currentTimeMillis(),
            intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0),
            intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0),
            intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10,
            intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0),
            batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER),
            intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0),
        )
        GlobalScope.launch { db?.batteryInfoDao()?.insertAll(batteryRaw) }
        Log.d("BatteryReceiver", "onReceive(): Triggered ")

    }
}
package com.example.androidDeviceDetails.models.batteryModels

import android.os.BatteryManager
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidDeviceDetails.cooker.BatteryCooker
import com.example.androidDeviceDetails.models.RoomDB
import com.example.androidDeviceDetails.receivers.BatteryCollector

/**
 * A data class used by the [BatteryCollector] to record a battery event and
 * save to the [RoomDB.batteryDao] and also used by the [BatteryCooker]
 * @param timeStamp Time of the record.
 * @param level Battery level.
 * @param plugged one of [BatteryManager.BATTERY_PLUGGED_AC],
 * [BatteryManager.BATTERY_PLUGGED_USB],
 * [BatteryManager.BATTERY_PLUGGED_WIRELESS],0 : if Device is on battery.
 * @param temp Battery temperature.
 * @param health one of
 * [BatteryManager.BATTERY_HEALTH_COLD],
 * [BatteryManager.BATTERY_HEALTH_DEAD],
 * [BatteryManager.BATTERY_HEALTH_GOOD],
 * [BatteryManager.BATTERY_HEALTH_OVERHEAT],
 * [BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE],
 * [BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE],
 * [BatteryManager.BATTERY_HEALTH_UNKNOWN]
 * @param estimatedCapacity Battery capacity in mAH
 * @param estimatedAccuracy Currently it is the same as [level]
 **/
@Entity
data class BatteryEntity(
    @PrimaryKey val timeStamp: Long,
    @ColumnInfo(name = "level") val level: Int?,
    @ColumnInfo(name = "plugged") val plugged: Int?,
    @ColumnInfo(name = "temp") val temp: Int?,
    @ColumnInfo(name = "health") val health: Int?,
    @ColumnInfo(name = "estimatedCapacity") val estimatedCapacity: Int,
    @ColumnInfo(name = "estimatedAccuracy") val estimatedAccuracy: Int?
)


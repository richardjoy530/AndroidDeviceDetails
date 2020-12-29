package com.example.androidDeviceDetails.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidDeviceDetails.DeviceDetailsApplication
import com.example.androidDeviceDetails.models.batteryModels.AppEventDao
import com.example.androidDeviceDetails.models.batteryModels.AppEventEntity
import com.example.androidDeviceDetails.models.batteryModels.BatteryDao
import com.example.androidDeviceDetails.models.batteryModels.BatteryEntity


@Database(
    entities = [AppEventEntity::class, BatteryEntity::class, LocationModel::class, Apps::class, AppHistory::class, WifiRaw::class, CellularRaw::class, AppDataUsage::class, DeviceDataUsage::class],
    version = 1
)
abstract class RoomDB : RoomDatabase() {
    abstract fun batteryDao(): BatteryDao
    abstract fun appEventDao(): AppEventDao
    abstract fun locationDao(): ILocationDao
    abstract fun appsDao(): AppsDao
    abstract fun appHistoryDao(): AppHistoryDao
    abstract fun wifiDao(): WifiDao
    abstract fun cellularDao(): CellularDao
    abstract fun appDataUsage(): AppDataUsageDao
    abstract fun deviceDataUsage(): DeviceDataUsageDao

    companion object {
        private var INSTANCE: RoomDB? = null
        fun getDatabase(context: Context = DeviceDetailsApplication.instance): RoomDB? {
            if (INSTANCE == null) {
                synchronized(RoomDB::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        RoomDB::class.java, "room_db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}
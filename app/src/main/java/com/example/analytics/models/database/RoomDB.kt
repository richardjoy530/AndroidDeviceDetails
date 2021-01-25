package com.example.analytics.models.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.analytics.DeviceDetailsApplication
import com.example.analytics.models.signal.SignalDao
import com.example.analytics.models.signal.SignalRaw


@Database(
    entities = [AppEventRaw::class, BatteryRaw::class, AppInfoRaw::class, AppHistoryRaw::class, SignalRaw::class, AppNetworkUsageRaw::class, DeviceNetworkUsageRaw::class],
    version = 1
)
abstract class RoomDB : RoomDatabase() {
    abstract fun batteryDao(): BatteryDao
    abstract fun appEventDao(): AppEventDao
    abstract fun appsDao(): AppInfoDao
    abstract fun appHistoryDao(): AppHistoryDao
    abstract fun signalDao(): SignalDao
    abstract fun appNetworkUsageDao(): AppNetworkUsageDao
    abstract fun deviceNetworkUsageDao(): DeviceNetworkUsageDao

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
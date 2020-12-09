package com.example.androidDeviceDetails

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LocationModel::class], version = 1, exportSchema = false)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun locationDao(): ILocationDao

    companion object {
        @Volatile
        private var INSTANCE: LocationDatabase? = null

        fun getDatabase(context: Context): LocationDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationDatabase::class.java,
                    "Location_Data.db"
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}
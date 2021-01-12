package com.example.androidDeviceDetails.models.batteryModels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidDeviceDetails.collectors.AppEventCollector
import com.example.androidDeviceDetails.cooker.BatteryCooker
import com.example.androidDeviceDetails.models.RoomDB

/**
 * A data class used by the [AppEventCollector] to record a battery event and
 * save to the [RoomDB.appEventDao] and also used by the [BatteryCooker]
 * @param timeStamp Time of the record.
 * @param packageName Name of the package.
 *
 * @see [RoomDB]
 * @see [AppEventDao]
 **/
@Entity
data class AppEventEntity(
    @PrimaryKey val timeStamp: Long,
    @ColumnInfo(name = "packageName ") val packageName: String,
)


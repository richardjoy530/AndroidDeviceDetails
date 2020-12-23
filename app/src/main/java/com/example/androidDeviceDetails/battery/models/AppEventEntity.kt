package com.example.androidDeviceDetails.battery.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppEventEntity(
    @PrimaryKey val timeStamp: Long,
    @ColumnInfo(name = "packageName ") val packageName: String,
)


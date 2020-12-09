package com.example.androidDeviceDetails

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "Location_Data")
data class LocationModel (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val latitude: Double?,
    val longitude: Double?,
    val geoHash: String?,
    val time : String?
)
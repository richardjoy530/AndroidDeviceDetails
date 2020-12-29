package com.example.androidDeviceDetails.models

import androidx.room.*

@Entity
data class DeviceDataUsage(
    @PrimaryKey val timeStamp: Long,
    @ColumnInfo(name = "transferredDataWifi") val transferredDataWifi: Long,
    @ColumnInfo(name = "transferredDataMobile") val transferredDataMobile: Long,
    @ColumnInfo(name = "receivedDataWifi ") val receivedDataWifi: Long,
    @ColumnInfo(name = "receivedDataMobile ") val receivedDataMobile: Long
)


@Dao
interface DeviceDataUsageDao {
    @Query("SELECT * FROM DeviceDataUsage")
    fun getAll(): List<DeviceDataUsage>

    @Query("SELECT * FROM DeviceDataUsage WHERE timeStamp BETWEEN (:startTime) AND (:endTime)")
    fun getAllBetween(startTime: Long, endTime: Long): List<DeviceDataUsage>

    @Query("DELETE FROM DeviceDataUsage")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg deviceDataUsage: DeviceDataUsage)

    @Delete
    fun delete(deviceDataUsage: DeviceDataUsage)
}

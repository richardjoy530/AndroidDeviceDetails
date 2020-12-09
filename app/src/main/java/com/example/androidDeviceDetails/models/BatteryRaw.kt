package com.example.androidDeviceDetails.models

import androidx.room.*

@Entity
data class BatteryRaw(
    @PrimaryKey val timeStamp: Long,
    @ColumnInfo(name = "level") val level: Int?,
    @ColumnInfo(name = "plugged") val plugged: Int?,
    @ColumnInfo(name = "temp") val temp: Int?,
    @ColumnInfo(name = "health") val health: Int?,
    @ColumnInfo(name = "estimatedCapacity") val estimatedCapacity: Int,
    @ColumnInfo(name = "estimatedAccuracy") val estimatedAccuracy: Int?
)

@Dao
interface BatteryInfoDao {
    @Query("SELECT * FROM batteryRaw")
    fun getAll(): List<BatteryRaw>

    @Query("SELECT * FROM batteryRaw WHERE timeStamp BETWEEN (:startTime) AND (:endTime)")
    fun getAllBetween(startTime: Long, endTime: Long): List<BatteryRaw>

    @Query("DELETE FROM batteryRaw")
    fun deleteAll()

    @Insert
    fun insertAll(vararg batteryRaw: BatteryRaw)

    @Delete
    fun delete(batteryRaw: BatteryRaw)
}
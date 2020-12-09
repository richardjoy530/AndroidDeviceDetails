package com.example.androidDeviceDetails.models

import androidx.room.*

@Entity
data class BatteryRawModel(
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
    @Query("SELECT * FROM BatteryRawModel")
    fun getAll(): List<BatteryRawModel>

    @Query("SELECT * FROM BatteryRawModel WHERE timeStamp BETWEEN (:startTime) AND (:endTime)")
    fun getAllBetween(startTime: Long, endTime: Long): List<BatteryRawModel>

    @Query("DELETE FROM BatteryRawModel")
    fun deleteAll()

    @Insert
    fun insertAll(vararg batteryRawModel: BatteryRawModel)

    @Delete
    fun delete(batteryRawModel: BatteryRawModel)
}
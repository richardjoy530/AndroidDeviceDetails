package com.example.androidDeviceDetails.models.networkUsageModels

import androidx.room.*

@Dao
/**
 * An interface that contains functions to handle database operations.
 */
interface DeviceNetworkUsageDao {
    /**
     * Retrieve all the records from [DeviceNetworkUsageDao]
     * @return A list of [DeviceNetworkUsageEntity].
     */
    @Query("SELECT * FROM DeviceNetworkUsageEntity")
    fun getAll(): List<DeviceNetworkUsageEntity>

    /**
     * Retrieve all the records from [DeviceNetworkUsageDao] between the [startTime] and [endTime]
     * @param startTime Start Time
     * @param endTime End Time
     * @return A list of [DeviceNetworkUsageDao].
     */
    @Query("SELECT * FROM DeviceNetworkUsageEntity WHERE timeStamp BETWEEN (:startTime) AND (:endTime)")
    fun getAllBetween(startTime: Long, endTime: Long): List<DeviceNetworkUsageEntity>

    @Query("DELETE FROM DeviceNetworkUsageEntity")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg deviceNetworkUsageEntity: DeviceNetworkUsageEntity)

    @Delete
    fun delete(deviceNetworkUsageEntity: DeviceNetworkUsageEntity)
}
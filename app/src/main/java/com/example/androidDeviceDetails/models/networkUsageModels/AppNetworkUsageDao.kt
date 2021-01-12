package com.example.androidDeviceDetails.models.networkUsageModels

import androidx.room.*

/**
 * An interface that contains functions to handle database operations.
 */
@Dao
interface AppNetworkUsageDao {
    /**
     * Retrieve all the records from [AppNetworkUsageDao]
     * @return A list of [AppNetworkUsageEntity].
     */
    @Query("SELECT * FROM AppNetworkUsageEntity")
    fun getAll(): List<AppNetworkUsageEntity>

    /**
     * Retrieve all the records from [AppNetworkUsageDao] between the [startTime] and [endTime]
     * @param startTime Start Time
     * @param endTime End Time
     * @return A list of [AppNetworkUsageEntity].
     */
    @Query("SELECT * FROM AppNetworkUsageEntity WHERE timeStamp BETWEEN (:startTime) AND (:endTime)")
    fun getAllBetween(startTime: Long, endTime: Long): List<AppNetworkUsageEntity>

    @Query("DELETE FROM AppNetworkUsageEntity")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg appNetworkUsageEntity: AppNetworkUsageEntity)

    @Delete
    fun delete(appNetworkUsageEntity: AppNetworkUsageEntity)
}
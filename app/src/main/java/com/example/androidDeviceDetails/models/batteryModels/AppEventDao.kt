package com.example.androidDeviceDetails.models.batteryModels

import androidx.room.*

/**
 * An interface that contains functions to handle database operations
 */
@Dao
interface AppEventDao {

    /**
     * Retrieve all the records from [AppEventDao]
     * @return List of [AppEventEntity]
     */
    @Query("SELECT * FROM AppEventEntity")
    fun getAll(): List<AppEventEntity>

    /**
     * Returns all the [AppEventEntity] in the given time frame
     * @param startTime Start time
     * @param endTime End time
     * @return List of [AppEventEntity]
     */
    @Query("SELECT * FROM AppEventEntity WHERE timeStamp BETWEEN (:startTime) AND (:endTime)")
    fun getAllBetween(startTime: Long, endTime: Long): List<AppEventEntity>

    @Query("DELETE FROM AppEventEntity")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg appEventEntity: AppEventEntity)

    @Delete
    fun delete(appEventEntity: AppEventEntity)
}
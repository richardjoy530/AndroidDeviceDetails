package com.example.androidDeviceDetails.models.appInfoModels

import androidx.room.*

/**
 * An interface that contains functions to handle database operations
 */
@Dao
interface AppHistoryDao {
    /**
     * Retrieve all the records from [AppHistoryDao]
     * @return List of [AppHistoryEntity]
     */
    @Query("SELECT * FROM AppHistoryEntity")
    fun getAll(): List<AppHistoryEntity>

    /**
     * Returns all the Id's of the apps in the given time interval
     * @param startTime Start time
     * @param endTime End time
     * @return List of app Id's
     */
    @Query("SELECT DISTINCT appId FROM AppHistoryEntity WHERE timestamp BETWEEN (:startTime) AND (:endTime) ")
    fun getIdsBetween(startTime: Long, endTime: Long): List<Int>

    /**
     * Returns the most recent record of the app in the given time interval
     * @param appId App Id from [AppsDao]
     * @param startTime Start time
     * @param endTime End time
     * @return Most recent entry of [AppHistoryEntity]
     *
     */
    @Query("SELECT * FROM AppHistoryEntity WHERE appId == (:appId) AND timestamp BETWEEN (:startTime) AND (:endTime) ORDER BY rowId DESC LIMIT 1")
    fun getLatestRecordBetween(appId: Int, startTime: Long, endTime: Long): AppHistoryEntity

    /**
     * Returns the oldest record of the app in the given time interval
     * @param appId App Id from [AppsDao]
     * @param startTime Start time
     * @param endTime End time
     * @return Oldest entry of [AppHistoryEntity]
     */
    @Query("SELECT * FROM AppHistoryEntity WHERE appId == (:appId) AND timestamp BETWEEN (:startTime) AND (:endTime) ORDER BY rowId ASC LIMIT 1")
    fun getInitialRecordBetween(appId: Int, startTime: Long, endTime: Long): AppHistoryEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: AppHistoryEntity)

    @Delete
    fun delete(user: AppHistoryEntity)
}
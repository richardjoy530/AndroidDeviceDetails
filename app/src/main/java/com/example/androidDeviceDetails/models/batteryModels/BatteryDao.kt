package com.example.androidDeviceDetails.models.batteryModels

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * An interface that contains functions to handle database operations
 */
@Dao
interface BatteryDao {

    /**
     * Retrieve all the records from [BatteryDao]
     * @return List of [BatteryEntity]
     */
    @Query("SELECT * FROM BatteryEntity")
    fun getAll(): List<BatteryEntity>

    /**
     * Returns all the [BatteryEntity] in the given time frame
     * @param startTime Start time
     * @param endTime End time
     * @return List of [BatteryEntity]
     */
    @Query("SELECT * FROM BatteryEntity WHERE timeStamp BETWEEN (:startTime) AND (:endTime)")
    fun getAllBetween(startTime: Long, endTime: Long): List<BatteryEntity>

    @Query("DELETE FROM BatteryEntity")
    fun deleteAll()

    @Insert
    fun insertAll(vararg batteryEntity: BatteryEntity)

    @Delete
    fun delete(batteryEntity: BatteryEntity)
}
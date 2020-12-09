package com.example.androidDeviceDetails.models

import androidx.room.*

@Entity
data class AppUsageModel(
    @PrimaryKey val timeStamp: Long,
    @ColumnInfo(name = "packageName ") val packageName: String,
)

@Dao
interface AppUsageInfoDao {
    @Query("SELECT * FROM AppUsageModel")
    fun getAll(): List<AppUsageModel>

    @Query("SELECT * FROM AppUsageModel WHERE timeStamp BETWEEN (:startTime) AND (:endTime)")
    fun getAllBetween(startTime: Long, endTime: Long): List<AppUsageModel>

    @Query("DELETE FROM AppUsageModel")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg appUsageModel: AppUsageModel)

    @Delete
    fun delete(appUsageModel: AppUsageModel)
}

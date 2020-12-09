package com.example.androidDeviceDetails.models

import androidx.room.*

@Entity
data class AppUsageRaw(
    @PrimaryKey val timeStamp: Long,
    @ColumnInfo(name = "packageName ") val packageName: String,
)

@Dao
interface AppUsageInfoDao {
    @Query("SELECT * FROM AppUsageRaw")
    fun getAll(): List<AppUsageRaw>

    @Query("SELECT * FROM AppUsageRaw WHERE timeStamp BETWEEN (:startTime) AND (:endTime)")
    fun getAllBetween(startTime: Long, endTime: Long): List<AppUsageRaw>

    @Query("DELETE FROM AppUsageRaw")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg appUsageRaw: AppUsageRaw)

    @Delete
    fun delete(appUsageRaw: AppUsageRaw)
}

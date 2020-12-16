package com.example.androidDeviceDetails.models

import androidx.room.*

@Entity
data class AppDataUsage(
    @PrimaryKey val timeStamp: Long,
    @ColumnInfo(name = "packageName ") val packageName: String,
    @ColumnInfo(name = "dataUsage ") val dataUsage: Long,
)

@Dao
interface AppDataUsageDao {
    @Query("SELECT * FROM AppDataUsage")
    fun getAll(): List<AppDataUsage>

    @Query("SELECT * FROM AppDataUsage WHERE timeStamp BETWEEN (:startTime) AND (:endTime)")
    fun getAllBetween(startTime: Long, endTime: Long): List<AppDataUsage>

    @Query("DELETE FROM AppDataUsage")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg appDataUsage: AppDataUsage)

    @Delete
    fun delete(appDataUsage: AppDataUsage)
}

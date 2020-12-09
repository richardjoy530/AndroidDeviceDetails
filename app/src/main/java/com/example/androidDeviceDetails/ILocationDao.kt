package com.example.androidDeviceDetails

import androidx.room.*

@Dao
interface ILocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(location : LocationModel)

    @Query("SELECT * FROM Location_Data")
    fun readAll(): List<LocationModel>

    @Query("SELECT geoHash, count(geoHash) AS count FROM Location_Data GROUP BY geoHash")
    fun countHash(): List<CountModel>
}
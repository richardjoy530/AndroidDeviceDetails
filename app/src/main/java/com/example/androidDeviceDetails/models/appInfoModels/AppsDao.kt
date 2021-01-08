package com.example.androidDeviceDetails.models.appInfoModels

import androidx.room.*

/**
 * An interface that contains functions to handle database operations
 */
@Dao
interface AppsDao {
    /**
     * Retrieve all the records from [AppsDao]
     * @return List of [AppsEntity]
     */
    @Query("SELECT * FROM AppsEntity")
    fun getAll(): List<AppsEntity>

    /**
     * Returns [AppsEntity] corresponding to [appId]
     * @param appId Id of the app in [AppsDao]
     * @return [AppsEntity] with the given [appId]
     */
    @Query("SELECT * FROM AppsEntity WHERE uid IN (:appId)")
    fun getById(appId: Int): AppsEntity

    /**
     * Returns [AppsEntity.uid] corresponding to [pName]
     * @param pName Name of the app
     * @return [AppsEntity] with the given [pName]
     */
    @Query("Select uid from AppsEntity WHERE package_name=(:pName)")
    fun getIdByName(pName: String?): Int

    /**
     * Returns Package name corresponding to [appId]
     * @param appId Id of the app in [AppsDao]
     * @return package name of the app with the given [appId]
     */
    @Query("Select package_name from AppsEntity WHERE uid =(:appId)")
    fun getPackageByID(appId: Int): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg apps: AppsEntity)

    @Delete
    fun delete(user: AppsEntity)
}
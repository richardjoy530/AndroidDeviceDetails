package com.example.androidDeviceDetails.models

import androidx.room.*

@Entity
@ForeignKey(entity = Apps::class, parentColumns = ["uid"], childColumns = ["appId"])
data class AppHistory(
    @PrimaryKey(autoGenerate = true) val rowId: Int,
    @ColumnInfo val appId: Int,
    @ColumnInfo(name = "timestamp") var timestamp: Long?,
    @ColumnInfo(name = "event_type") var eventType: Int?,
    @ColumnInfo(name = "version_name") var versionName: String?,
    @ColumnInfo(name = "version_code") var versionCode: Int?,
    @ColumnInfo(name = "apk_size") var appSize: Long?
)


@Dao
interface AppHistoryDao {
    @Query("SELECT * FROM AppHistory")
    fun getAll(): List<AppHistory>

    @Query("SELECT * FROM AppHistory WHERE appId IN (:userIds)")
    fun getById(userIds: Int): AppHistory

    @Query("SELECT * FROM AppHistory WHERE appId == (:appId) ORDER BY rowId DESC LIMIT 1")
    fun getLastRecord(appId : Int): AppHistory

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: AppHistory)

    @Delete
    fun delete(user: AppHistory)
}

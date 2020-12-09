package com.example.androidDeviceDetails.models

import androidx.room.*

@Entity
data class CellularRaw(
    @PrimaryKey val timeStamp: Long,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "strength") val strength: Int?,
    @ColumnInfo(name = "level") val level: Int?,
    @ColumnInfo(name = "asuLevel") val asuLevel: Int?
)

@Dao
interface CellularDao {
    @Query("SELECT * FROM CellularRaw")
    fun getAll(): List<CellularRaw>

    @Insert
    fun insertAll(vararg cellularRaw: CellularRaw)

    @Delete
    fun delete(cellularRaw: CellularRaw)
}
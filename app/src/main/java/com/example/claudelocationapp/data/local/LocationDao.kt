package com.example.claudelocationapp.data.local

import androidx.room.*
import com.example.claudelocationapp.data.model.LocationPoint
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationPoint)

    @Query("SELECT * FROM location_points WHERE timestamp BETWEEN :startTime AND :endTime")
    fun getLocationsForDateRange(startTime: Long, endTime: Long): Flow<List<LocationPoint>>

    @Query("SELECT * FROM location_points WHERE uploaded = 0")
    suspend fun getPendingUploads(): List<LocationPoint>

    @Query("UPDATE location_points SET uploaded = 1 WHERE id IN (:ids)")
    suspend fun markUploaded(ids: List<Long>)
}
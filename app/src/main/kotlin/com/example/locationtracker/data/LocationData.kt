package com.example.locationtracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "location_data")
data class LocationData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val activity: String,
    val timestamp: LocalDateTime,
    val uploaded: Boolean = false
)

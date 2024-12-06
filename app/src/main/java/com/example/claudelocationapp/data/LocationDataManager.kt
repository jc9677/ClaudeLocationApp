package com.example.claudelocationapp.data

import android.location.Location
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit

object LocationDataManager {
    private val locations = mutableListOf<LocationData>()
    private var lastUploadTime: Long = 0

    fun addLocation(location: Location, activity: String = "UNKNOWN") {
        val locationData = LocationData(
            latitude = location.latitude,
            longitude = location.longitude,
            timestamp = System.currentTimeMillis(),
            activity = activity
        )
        locations.add(locationData)
        checkAndUpload()
    }

    private fun checkAndUpload() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastUploadTime >= TimeUnit.HOURS.toMillis(1)) {
            CoroutineScope(Dispatchers.IO).launch {
                uploadToDigitalOcean()
            }
        }
    }

    private suspend fun uploadToDigitalOcean() {
        // Implement Digital Ocean upload logic
        lastUploadTime = System.currentTimeMillis()
    }

    fun getLocationsForTimeRange(startTime: Long, endTime: Long): List<LocationData> {
        return locations.filter { it.timestamp in startTime..endTime }
    }

    data class LocationData(
        val latitude: Double,
        val longitude: Double,
        val timestamp: Long,
        val activity: String
    )
}
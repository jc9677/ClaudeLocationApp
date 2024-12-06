package com.example.claudelocationapp.data

import com.example.claudelocationapp.data.local.LocationDao
import com.example.claudelocationapp.data.model.LocationPoint
import com.example.claudelocationapp.data.remote.DigitalOceanStorage
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val locationDao: LocationDao,
    private val digitalOceanStorage: DigitalOceanStorage
) {
    suspend fun saveLocation(location: LocationPoint) =
        locationDao.insert(location)

    fun getLocationsForDateRange(startTime: Long, endTime: Long) =
        locationDao.getLocationsForDateRange(startTime, endTime)

    suspend fun uploadLocations() {
        val pendingLocations = locationDao.getPendingUploads()
        digitalOceanStorage.uploadLocations(pendingLocations)
        locationDao.markUploaded(pendingLocations.map { it.id })
    }
}
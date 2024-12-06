package com.example.claudelocationapp.service

import android.app.*
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.ActivityRecognition
import java.util.concurrent.TimeUnit

class LocationTrackingService : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var activityRecognitionClient: ActivityRecognitionClient
    private var lastLocation: Location? = null
    
    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        activityRecognitionClient = ActivityRecognition.getClient(this)
        startForeground(NOTIFICATION_ID, createNotification())
        startLocationUpdates()
        startActivityRecognition()
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(10)
            fastestInterval = TimeUnit.SECONDS.toMillis(5)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { newLocation ->
                if (lastLocation == null || 
                    lastLocation!!.distanceTo(newLocation) >= 20) {
                    // Log location and check for upload
                    LocationDataManager.addLocation(newLocation)
                    lastLocation = newLocation
                }
            }
        }
    }

    // ... Additional service implementation
    
    companion object {
        private const val NOTIFICATION_ID = 12345
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
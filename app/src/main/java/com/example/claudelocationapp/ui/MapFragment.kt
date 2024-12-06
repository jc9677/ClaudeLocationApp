package com.example.claudelocationapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.claudelocationapp.R
import com.example.claudelocationapp.data.LocationDataManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import java.util.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class MapFragment : Fragment(), OnMapReadyCallback {
    private var map: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun displayTodayLocations() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()

        val locations = LocationDataManager.getLocationsForTimeRange(startTime, endTime)
        
        if (locations.isEmpty()) return

        val bounds = LatLngBounds.builder()
        val points = mutableListOf<LatLng>()

        // Add markers and collect points for polyline
        locations.forEach { location ->
            val position = LatLng(location.latitude, location.longitude)
            points.add(position)
            bounds.include(position)
            
            map?.addMarker(
                MarkerOptions()
                    .position(position)
                    .title("${location.timestamp.formatDateTime()}")
                    .snippet("Activity: ${location.activity}")
            )
        }

        // Draw path line
        map?.addPolyline(
            PolylineOptions()
                .addAll(points)
                .color(resources.getColor(R.color.path_color, null))
                .width(5f)
        )

        // Animate camera to show all points
        val padding = resources.getDimensionPixelSize(R.dimen.map_padding)
        map?.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                padding
            )
        )
    }

    private fun Long.formatDateTime(): String {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = this@formatDateTime
        }
        return String.format(
            "%02d:%02d",
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE)
        )
    }
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        setupMap()
        displayTodayLocations()
    }

    private fun setupMap() {
        map?.apply {
            // Enable zoom controls
            uiSettings.isZoomControlsEnabled = true
            
            // Enable my location button (will only work if permission is granted)
            try {
                isMyLocationEnabled = true
                uiSettings.isMyLocationButtonEnabled = true
            } catch (e: SecurityException) {
                // Handle permission not granted
            }
        }
    }
    
}
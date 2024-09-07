package com.example.wherekiddo.util.location

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.location.LocationManager.NETWORK_PROVIDER
import android.os.Looper
import com.example.wherekiddo.util.extensions.hasLocationPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

private const val LOCATION_PERMISSION_NOT_GRANTED_MESSAGE = "Location permission not granted!"
private const val GPS_DISABLED_MESSAGE = "GPS is disabled!"

class DefaultLocationClient(
    private val context: Context,
    private val client: FusedLocationProviderClient
): LocationClient {

    override fun getLocationUpdates(interval: Long): Flow<Location> = callbackFlow {

        if (!context.hasLocationPermission()) {

            throw LocationClient.LocationException(LOCATION_PERMISSION_NOT_GRANTED_MESSAGE)
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(NETWORK_PROVIDER)

        if (!isGPSEnabled && !isNetworkEnabled) {

            throw LocationClient.LocationException(GPS_DISABLED_MESSAGE)
        }

        val request = LocationRequest.Builder(interval).build()

        val locationCallback = object: LocationCallback() {

            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)

                result.lastLocation?.let { launch { send(it) } }
            }
        }

        client.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())

        awaitClose { client.removeLocationUpdates(locationCallback) }
    }
}
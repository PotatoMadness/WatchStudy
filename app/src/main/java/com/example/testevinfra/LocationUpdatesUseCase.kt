package com.evinfra.data.usecase

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log
import com.evinfra.data.model.map.EIMapPoint
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class LocationUpdatesUseCase @Inject constructor(
    private val client: FusedLocationProviderClient,
    private val looper: Looper
) {
    var lastLocation: EIMapPoint = DEF_LOCATION

    @SuppressLint("MissingPermission")
    private val _locationUpdates = callbackFlow {
        val callback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let {
                    val newLoc = EIMapPoint(it.latitude, it.longitude)
                    lastLocation = newLoc
                    trySend(newLoc)
                }
            }
        }
        Log.d("SharedLocationProvider", "Starting location updates ")

        val locationRequest = LocationRequest.create()
            .setWaitForAccurateLocation(false)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL_SECS)
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_SECS)
        try {
            client.requestLocationUpdates(
                locationRequest,
                callback,
                looper
            )
        } catch (e: Exception) {
            close(e) // in case of exception, close the Flow
        }

        awaitClose {
            Log.d("SharedLocationProvider", "Stopping location updates")
            client.removeLocationUpdates(callback) // clean up when Flow collection ends
        }
    }

    val locationUpdates: Flow<EIMapPoint> = _locationUpdates

    companion object {
        private const val UPDATE_INTERVAL_SECS = 10L
        private const val FASTEST_UPDATE_INTERVAL_SECS = 1L
        private val DEF_LOCATION = EIMapPoint(37.5666101, 126.97838810)
    }
}
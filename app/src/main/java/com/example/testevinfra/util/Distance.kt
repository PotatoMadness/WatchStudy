package com.example.testevinfra.util

import android.location.Location
import android.location.LocationManager
import kotlin.math.roundToInt

fun Location?.getDistance(lat: String, lng: String) : Float?{
    if (this == null) return null
    val targetLoc = Location(LocationManager.NETWORK_PROVIDER)
    targetLoc.latitude = lat.toDouble()
    targetLoc.longitude = lng.toDouble()
    val dist = this.distanceTo(targetLoc) / 1000
    return dist.roundToInt().toFloat()
}

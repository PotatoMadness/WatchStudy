package com.example.testevinfra.util

import android.location.Location
import android.location.LocationManager
import kotlin.math.roundToInt

fun Location?.getDistance(lat: String, lng: String) : Float?{
    return try {
        if (this == null) return throw Exception("현재위치 없음")
        val targetLoc = Location(LocationManager.NETWORK_PROVIDER)
        targetLoc.latitude = lat.toDouble()
        targetLoc.longitude = lng.toDouble()
        val dist = this.distanceTo(targetLoc) / 1000
        dist.roundToInt().toFloat()
    } catch (e: Exception) {
        null
    }
}

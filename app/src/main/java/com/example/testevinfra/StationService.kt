package com.example.testevinfra

import retrofit2.http.GET
import retrofit2.http.Query

interface StationService {
    @GET("/test/test/station_list")
    suspend fun getStationList(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String
    ): ResponseStationInfo
}
package com.example.testevinfra

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val repository = StationRepository()

    private fun stationInfos(lat: String, lon: String): Flow<List<StationInfo>> =
        repository.getImageSearch(query)
}
package com.example.testevinfra

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    locationUpdatesUseCase: LocationUpdatesUseCase
) : ViewModel() {
    private val repository = StationRepository()
    val stationList = MutableStateFlow<ResultStationInfo?>(null)
    val lastLocation = locationUpdatesUseCase.locationUpdates.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )
    var timestamp = MutableStateFlow("00시 00분 기준")

    fun getStationInfo(loc: Location) {
        viewModelScope.launch(Dispatchers.IO) {
            stationList.emit(ResultStationInfo.IsLoading)
            val result = repository.getStationList(loc.latitude, loc.longitude)
            when (result) {
                is ResultStationInfo.Success -> {
                    stationList.emit(result)
                    val sdf = SimpleDateFormat("hh시 mm분 기준")
                    val currentDateAndTime = sdf.format(Date())
                    timestamp.emit(currentDateAndTime)
                }
                else -> {
                    stationList.emit(ResultStationInfo.Failed(Exception("failed")))
                }
            }
        }
    }

    fun refresh() {
        Log.d("asdf", "refresh")
        lastLocation.value?.let { getStationInfo(it) }
    }
}
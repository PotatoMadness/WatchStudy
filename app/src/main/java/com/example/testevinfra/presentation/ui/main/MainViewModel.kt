package com.example.testevinfra.presentation.ui.main

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testevinfra.LocationUpdatesUseCase
import com.example.testevinfra.ResultStationInfo
import com.example.testevinfra.data.StationInfo
import com.example.testevinfra.data.StationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    locationUpdatesUseCase: LocationUpdatesUseCase
) : ViewModel() {
    private val repository = StationRepository()
    // Holds our view state which the UI collects via [state]
    private val _state = MutableStateFlow(MainViewState())

    val state: StateFlow<MainViewState>
        get() = _state

    val stationList = MutableStateFlow<List<StationInfo>>(arrayListOf())
    val selectedStation = MutableStateFlow<StationInfo?>(null)
    private val refreshing = MutableStateFlow(true)

    val lastLocation = locationUpdatesUseCase.locationUpdates.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val savedLocation: MutableStateFlow<Location?> = MutableStateFlow(null)

    private var _timeStamp = MutableStateFlow("00시 00분 기준")

    init {
        viewModelScope.launch {
            // Combines the latest value from each of the flows, allowing us to generate a
            // view state instance which only contains the latest values.
            combine(
                savedLocation,
                stationList,
                selectedStation,
                refreshing,
            ) { location, stationList, selectedStation, refreshing ->
                MainViewState(
                    currentLocation = location,
                    stationList = stationList,
                    selectedStation = selectedStation,
                    refreshing = refreshing,
                    errorMessage = null /* TODO */,
                    timeStamp = _timeStamp.value
                )
            }.catch { throwable ->
                // TODO: emit a UI error here. For now we'll just rethrow
                throw throwable
            }.collect {
                _state.value = it
            }
        }

        viewModelScope.launch {
            lastLocation.collect {
                if (it == null) return@collect
                refreshing.value = true
                if (stationList.value.isEmpty()) {
                    refresh(it)
                }
            }
        }
    }

    private fun refresh(loc: Location) {
        viewModelScope.launch {
            runCatching {
                savedLocation.value = loc
                refreshing.value = true
                val result = repository.getStationList(loc.latitude, loc.longitude)
                if (result is ResultStationInfo.Success) {
                    stationList.emit(result.result.list)
                    val sdf = SimpleDateFormat("hh시 mm분 기준")
                    val currentDateAndTime = sdf.format(Date())
                    _timeStamp.emit(currentDateAndTime)
                }
            }
            // TODO: look at result of runCatching and show any errors

            refreshing.value = false
        }
    }

    fun onStationSelected(station: StationInfo) {
        selectedStation.value = station
    }

    fun updateList() {
        viewModelScope.launch {
            lastLocation.first()?.let { refresh(it) }
        }
    }
}

data class MainViewState(
    val currentLocation: Location? = null,
    val stationList: List<StationInfo> = emptyList(),
    val refreshing: Boolean = true,
    val selectedStation: StationInfo? = null,
    val errorMessage: String? = null,
    val timeStamp: String = ""
)
package com.example.testevinfra

import com.example.testevinfra.data.StationInfo

sealed class ResultStationInfo {
    object IsLoading: ResultStationInfo()
    data class Success(val result :ResponseStationInfo): ResultStationInfo()
    data class Failed(val e: Throwable): ResultStationInfo()
}
data class ResponseStationInfo (
    val code: Int,
    val list: List<StationInfo>
    )
package com.example.testevinfra.presentation.navigation


sealed class Screen(
    val route: String
) {
    object Main : Screen("main")
    object StationList : Screen("stationList")
    object StationDetail : Screen("stationDetail")
}
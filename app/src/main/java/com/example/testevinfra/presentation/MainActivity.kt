package com.example.testevinfra.presentation

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.currentBackStackEntryAsState
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.testevinfra.presentation.navigation.Screen
import com.example.testevinfra.presentation.theme.EVITheme
import com.example.testevinfra.presentation.ui.detail.DetailScreen
import com.example.testevinfra.presentation.ui.main.LoadingScreen
import com.example.testevinfra.presentation.ui.main.Main
import com.example.testevinfra.presentation.ui.main.MainViewModel
import com.example.testevinfra.presentation.ui.station.StationScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    internal lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("asdf", "dimen : " + resources.configuration.isScreenRound)

        setContent {
            navController = rememberSwipeDismissableNavController()
            WearApp(viewModel = viewModel,
                swipeDismissableNavController = navController)
        }

//        initView()
//        initObserver()
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            666
        )
    }

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
//                launch {
//                    viewModel.lastLocation.filterNotNull().collect {
//                        if (viewModel.stationList.value == null) {
//                            viewModel.getStationInfo(it)
//                        }
//                        val gCoder = Geocoder(this@MainActivity, Locale.KOREA)
//                        val addr = gCoder.getFromLocation(it.latitude, it.longitude, 1) ?: return@collect
//                        binding.tvLoc.text = addr[0].thoroughfare
//                    }
//                }
//                launch {
//                    viewModel.stationList.collect {
//                        when (it) {
//                            is ResultStationInfo.IsLoading -> {
//                                // loading animation
//                            }
//                            is ResultStationInfo.Success -> {
//                                val cnt = it.result.list.count { station ->
//                                    station.cl.any { charger ->
//                                        charger.cst == "3"
//                                    }
//                                }
//                                binding.tvEnableCount.text = "${cnt}곳 충전가능"
//                                binding.tvTimestamp.text = viewModel.timestamp.value
//                            }
//                        }
//                    }
//                }
            }
        }
    }

}

@Composable
fun WearApp(
    viewModel: MainViewModel,
    swipeDismissableNavController: NavHostController = rememberSwipeDismissableNavController()
) {
    EVITheme {
        val currentBackStackEntry by swipeDismissableNavController.currentBackStackEntryAsState()

        SwipeDismissableNavHost(
            navController = swipeDismissableNavController,
            startDestination = Screen.Loading.route,
            modifier = Modifier.background(androidx.wear.compose.material.MaterialTheme.colors.background)
        ) {
            // Main Window
            composable(
                route = Screen.Main.route,

            ) {
                Main (onClick = {swipeDismissableNavController.navigate(Screen.StationList.route)},
                viewModel = viewModel)

//                RequestFocusOnResume(focusRequester)
            }

            composable(
                route = Screen.StationList.route,
            ) {
                StationScreen (
                    viewModel = viewModel,
                    onClick = { swipeDismissableNavController.navigate(Screen.StationDetail.route) }
                )
            }

            composable(route = Screen.StationDetail.route) {
                DetailScreen(
                    viewModel = viewModel,)
            }

            composable(route = Screen.Loading.route) {
                LoadingScreen(viewModel = viewModel,
                    onLoadDone = { swipeDismissableNavController.navigate(Screen.Main.route) })
            }
        }
    }
}

@Composable
private fun RequestFocusOnResume(focusRequester: FocusRequester) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.RESUMED) {
            focusRequester.requestFocus()
        }
    }
}


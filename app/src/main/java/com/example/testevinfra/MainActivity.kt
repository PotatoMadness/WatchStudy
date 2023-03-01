package com.example.testevinfra

import android.Manifest
import android.location.Geocoder
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.testevinfra.theme.EVITheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WearApp("Android")
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
                launch {
                    viewModel.lastLocation.filterNotNull().collect {
//                        if (viewModel.stationList.value == null) {
//                            viewModel.getStationInfo(it)
//                        }
//                        val gCoder = Geocoder(this@MainActivity, Locale.KOREA)
//                        val addr = gCoder.getFromLocation(it.latitude, it.longitude, 1) ?: return@collect
//                        binding.tvLoc.text = addr[0].thoroughfare
                    }
                }
                launch {
                    viewModel.stationList.collect {
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
                    }
                }
            }
        }
    }

}

@Composable
fun WearApp(greetingName: String) {
    EVITheme {
        /* If you have enough items in your list, use [ScalingLazyColumn] which is an optimized
         * version of LazyColumn for wear devices with some added features. For more information,
         * see d.android.com/wear/compose.
         */
        Main(greetingName = greetingName)
    }
}

@Composable
fun Main(greetingName: String) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .selectableGroup(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_map_point),
            contentDescription = null,
            Modifier.padding(4.dp)
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            text = "근처 충전소"
        )
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(MaterialTheme.colorScheme.onPrimaryContainer)
            ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                text = "지역명"
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.labelLarge,
                text = "0곳 충전가능"
            )
        }
        Box(modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer)) {
            Image(
                painter = painterResource(id = R.drawable.icon_refresh),
                contentDescription = null,
                Modifier.padding(4.dp)
            )
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}

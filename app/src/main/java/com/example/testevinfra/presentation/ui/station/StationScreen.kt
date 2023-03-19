package com.example.testevinfra.presentation.ui.station

import android.content.res.Configuration
import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testevinfra.R
import com.example.testevinfra.data.StationInfo
import com.example.testevinfra.presentation.ui.main.MainViewModel
import com.example.testevinfra.util.getDistance

@Composable
fun StationScreen(
    viewModel: MainViewModel,
    onClick: () -> Unit = {}
) {
    val viewState by viewModel.state.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        StationRow(
            stationList = viewState.stationList,
            currentLocation = viewState.currentLocation,
            onClick = { station ->
                viewModel.onStationSelected(station)
                onClick.invoke()
            }
        )
    }
}

@Composable
fun StationRow(
    stationList: List<StationInfo>,
    onClick: (StationInfo) -> Unit,
    currentLocation: Location?
) {
    val lastIndex = stationList.size - 1
    LazyRow(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(items = stationList) { index: Int, item ->
            if (index == 0) {
                Spacer(modifier = Modifier
                    .width(19.dp)
                    .fillMaxHeight())
            }
            if (LocalConfiguration.current.isScreenRound) {
                Box(
                    modifier = Modifier
                        .size(154.dp, 154.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(listOf(Color(0x33DDE1E6), Color(0x21DDE1E6)))
                        )
                        .border(
                            brush = Brush.linearGradient(
                                listOf(
                                    Color(0xFF18CC8B),
                                    Color(0x0018CC8B)
                                )
                            ),
                            shape = CircleShape,
                            width = 1.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    StationInfoRowItem(
                        distance = currentLocation.getDistance(item.lat, item.lon),
                        stationInfo = item,
                        onClick = { onClick(item) }
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(144.dp, 144.dp)
                        .background(
                            brush = Brush.linearGradient(listOf(Color(0x33DDE1E6), Color(0x21DDE1E6)))
                        )
                        .border(
                            shape = RoundedCornerShape(50),
                            width = 1.dp, color = MaterialTheme.colorScheme.surface
                        )
                ) {
                    StationInfoRowItem(
                        distance = currentLocation.getDistance(item.lat, item.lon),
                        stationInfo = item,
                        onClick = { onClick(item) }
                    )
                }
            }
            if (index < lastIndex) Spacer(Modifier.width(8.dp))
            if (index == lastIndex) {
                Spacer(modifier = Modifier
                    .width(19.dp)
                    .fillMaxHeight())
                Box(modifier = Modifier.width(192.dp),
                    contentAlignment = Alignment.Center) {
                    Text(text = "인근 ${stationList.size}개의 충전소를\n모두 불러왔어요.",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}


@Composable
private fun StationInfoRowItem(
    distance: Float? = 0.0F,
    stationInfo: StationInfo,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.icon_map_point),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary),
            )
            Text(
                text = "${distance}m",
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(top = 2.dp),
                textAlign = TextAlign.Center
            )
        }

        Text(
            text = stationInfo.op,
            color = MaterialTheme.colorScheme.tertiaryContainer,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = stationInfo.snm,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
            textAlign = TextAlign.Center
        )

        if (stationInfo.standard.isNotEmpty()) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp)) {
                Image(painter = painterResource(id = R.drawable.icon_electric),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface))
                Text(
                    text = "완속",
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${stationInfo.standardActive.size}/${stationInfo.standard.size}",
                    color = MaterialTheme.colorScheme.surface,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(start = 4.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        if (stationInfo.fast.isNotEmpty()) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp)) {
                Image(painter = painterResource(id = R.drawable.icon_electric),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface))
                Text(
                    text = "급속",
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${stationInfo.fastActive.size}/${stationInfo.fast.size}",
                    color = MaterialTheme.colorScheme.surface,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(start = 2.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Preview(
    apiLevel = 26,
    uiMode = Configuration.UI_MODE_TYPE_WATCH,
    showSystemUi = true,
    device = Devices.WEAR_OS_SMALL_ROUND
)
@Composable
fun PreviewEpisodeListItem() {
    StationRow(
        stationList = listOf(
            StationInfo(
                snm = "서울 강남효성해링턴타워 2", cl = arrayListOf(),
                id = "1234", op = "이카플러그",
                lat = "37.491281", lon = "127.029276",
                roof = "0", st = 2
            ),
//            StationInfo(
//                snm = "asdf", cl = arrayListOf(),
//                id = "1234", op = "GS",
//                lat = "37.491281", lon = "127.029276"
//            )
        ),
        onClick = {},
        currentLocation = null
    )
}

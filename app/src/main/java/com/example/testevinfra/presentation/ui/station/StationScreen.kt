package com.example.testevinfra.presentation.ui.station

import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testevinfra.StationInfo
import com.example.testevinfra.presentation.ui.main.MainViewModel
import com.example.testevinfra.util.getDistance

@Composable
fun StationScreen(
    viewModel: MainViewModel,
    onClick: () -> Unit = {}) {
    val viewState by viewModel.state.collectAsState()
    StationRow(
        modifier = Modifier.padding(4.dp),
        stationList = viewState.stationList,
        currentLocation = viewState.currentLocation,
        onClick = { station ->
            viewModel.onStationSelected(station)
            onClick.invoke()
        }
    )
}

@Composable
fun StationRow (
    stationList: List<StationInfo>,
    onClick: (StationInfo) -> Unit,
    currentLocation: Location?,
    modifier: Modifier = Modifier
    ) {
        val lastIndex = stationList.size - 1
        LazyRow(
            modifier = modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimaryContainer),
            contentPadding = PaddingValues(
                start = 8.dp, top = 8.dp,
                end = 8.dp, bottom = 24.dp)
        ) {
            itemsIndexed(items = stationList) { index: Int, item ->
                StationInfoRowItem(
                    distance = currentLocation.getDistance(item.lat, item.lon),
                    stationName = item.snm,
                    operatorName = item.op,
                    onClick = { onClick(item) },
                    modifier = Modifier.width(128.dp)
                )

                if (index < lastIndex) Spacer(Modifier.width(24.dp))
            }
        }
}


@Composable
private fun StationInfoRowItem(
    distance: Float? = 0.0F,
    stationName: String,
    operatorName: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = "${distance}m",
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        )

        Text(
            text = stationName,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        )

        Text(
            text = operatorName,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        )
    }
}


@Preview
@Composable
fun PreviewEpisodeListItem() {
    StationInfoRowItem(distance = 0.0f,
    stationName = "asdf",
    operatorName = "qewrt ",
    onClick = {})
}

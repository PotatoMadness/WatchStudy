package com.example.testevinfra.presentation.ui.detail

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.testevinfra.data.Charger
import com.example.testevinfra.data.StationInfo
import com.example.testevinfra.presentation.theme.EVITheme
import com.example.testevinfra.presentation.ui.main.MainViewModel
import com.example.testevinfra.presentation.ui.main.MainViewState
import kotlinx.coroutines.selects.select

@Composable
fun DetailScreen(
    viewModel: MainViewModel
) {
    val viewState by viewModel.state.collectAsState()
    val selectedStation = viewState.selectedStation ?: return

    DetailScreen(
        selectedStation = selectedStation)
}

@Composable
fun DetailScreen(
    selectedStation: StationInfo,
){
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        // TODO :: scroll bar 표시
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            DetailHead(
                height = screenHeight,
                width = screenWidth, stationInfo = selectedStation
            )
            DetailBody(stationInfo = selectedStation)
            Spacer(modifier = Modifier.height((screenHeight/2).dp))
        }
    }
}

@Composable
fun DetailHead(
    height: Int,
    width: Int,
    distance: Float? = 0.0F,
    stationInfo: StationInfo
) {
    Column(
        modifier = Modifier
            .width(width = width.dp)
            .height(height = height.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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

        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stationInfo.lmStr,
                color = MaterialTheme.colorScheme.secondaryContainer,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(3.dp),
                    )
                    .padding(start = 4.dp, top = 2.dp, bottom = 2.dp, end = 4.dp)
            )

            if (stationInfo.roofStr.isNotEmpty()) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stationInfo.roofStr,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.onPrimary,
                            shape = RoundedCornerShape(3.dp)
                        )
                        .padding(start = 4.dp, top = 2.dp, bottom = 2.dp, end = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ChargerTypeHeader(
    title: String,
    cntStr: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.icon_electric),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface)
        )
        Text(
            text = title,
            color = MaterialTheme.colorScheme.tertiaryContainer,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Text(
            text = cntStr,
            color = MaterialTheme.colorScheme.surface,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(start = 4.dp),
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun DetailBody(
    stationInfo: StationInfo
) {
    stationInfo.fast.let {
        if (it.isEmpty()) return@let
        ChargerTypeHeader("급속", "${stationInfo.fastActive.size}/${stationInfo.fast.size}")
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stationInfo.totalType,
            color = MaterialTheme.colorScheme.secondaryContainer,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(3.dp),
                )
                .padding(start = 4.dp, top = 2.dp, bottom = 2.dp, end = 4.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        it.forEach { item ->
            ChargerDetail(item)
        }
    }

    stationInfo.standard.let {
        if (it.isEmpty()) return@let
        ChargerTypeHeader("완속", "${stationInfo.standardActive.size}/${stationInfo.standard.size}")
        Spacer(modifier = Modifier.height(12.dp))

        it.forEach { item ->
            ChargerDetail(item)
        }
    }
}


@Composable
fun ChargerDetail(
    charger: Charger
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(8.dp),
                brush = Brush.linearGradient(listOf(Color(0x33DDE1E6), Color(0x21DDE1E6)))
            )
            .let {
                if (charger.cst == "2") {
                    return@let it.border(
                        brush = Brush.linearGradient(
                            listOf(Color(0xFF18CC8B), Color(0x0018CC8B))
                        ),
                        shape = RoundedCornerShape(8.dp),
                        width = 1.dp
                    )
                }
                it
            }
            .padding(start = 16.dp, top = 14.dp, bottom = 14.dp, end = 16.dp)
    ) {
        Text(
            text = charger.p + "kW",
            color = MaterialTheme.colorScheme.tertiaryContainer,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Text(
            text = charger.rd ?: "충전시간 없음",
            color = MaterialTheme.colorScheme.tertiaryContainer,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Text(
            text = "최근 충전 속도 : " + charger.s,
            color = MaterialTheme.colorScheme.tertiaryContainer,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}


@Preview
@Composable
fun previewDetail() {
    ChargerDetail(Charger("11096","2","50","1", "1시간 32분 22초 전", "", "43kwh", ""))
}
val list = listOf(Charger("11056","2","80","1", "충전 중", "", "-", ""),
    Charger("11096","2","10","1", "충전 중", "", "-", ""),
    Charger("11096","8","50","1", "12시간 전", "", "43kwh", ""),
    Charger("11096","8","100","1", "1시간 32분 22초 전", "", "43kwh", ""),
)
val station = StationInfo(
    id = "115450",
    snm = "GS칼텍스 마전주유소",
    op = "GS칼텍스",
    lat = "37.593308",
    lon = "126.676002",
    cl = list,
    roof = "0",
    st = 6
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = true,
    device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
fun previewHeader() {
    EVITheme {
        DetailScreen(selectedStation = station)
    }
}

@Preview(widthDp = 180, heightDp = 600)
@Composable
fun previewBody() {
    EVITheme {
        Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            DetailBody(stationInfo = station)
        }
    }
}
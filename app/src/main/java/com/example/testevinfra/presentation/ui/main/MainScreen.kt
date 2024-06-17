package com.example.testevinfra.presentation.ui.main

import android.content.res.Configuration
import android.location.Geocoder
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testevinfra.R
import com.example.testevinfra.presentation.theme.EVITheme
import java.util.*

@Composable
fun Main(onClick: () -> Unit = {},
         viewModel: MainViewModel = viewModel()) {
    val viewState by viewModel.state.collectAsState()
    var addr = "알수 없음"
    viewState.currentLocation?.let{
        val gCoder = Geocoder(LocalContext.current, Locale.KOREA)
        val locInfo = gCoder.getFromLocation(it.latitude, it.longitude, 1) ?: return@let
        addr = locInfo[0].thoroughfare
    }
    MainContainer(
        onClick = onClick,
        addr = addr,
        size = viewState.stationList.size,
        onUpdate = { viewModel.updateList() }
    )
}

@Composable
fun MainContainer(
    onClick: () -> Unit,
    addr: String,
    size: Int,
    timeStamp: String = "00시 00분 기준",
    onUpdate: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .selectableGroup()
        .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_map_point),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary),
            modifier = Modifier.padding(11.dp)
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.labelSmall,
            text = addr
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            text = "근처 충전소"
        )
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(
                brush = Brush.linearGradient(listOf(Color(0x33DDE1E6), Color(0x21DDE1E6)))
            )
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 7.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                text = timeStamp,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 7.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.surface,
                style = MaterialTheme.typography.titleSmall,
                text = "${size}곳 충전가능"
            )
        }
        Box(modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onPrimary)
            .clickable(onClick = { onUpdate })
            .padding(4.dp)) {
            Image(
                painter = painterResource(id = R.drawable.icon_refresh),
                contentDescription = null
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = true,
    device = Devices.WEAR_OS_SMALL_ROUND,
)
@Composable
fun prevMainView() {
    EVITheme {
        Box {
            MainContainer(
                onClick = { },
                addr = "역삼동",
                size = 1,
                onUpdate = { })
        }
    }
}

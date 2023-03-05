package com.example.testevinfra.presentation.ui.main

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testevinfra.R
import java.util.*

@Composable
fun Main(onClick: () -> Unit = {},
         viewModel: MainViewModel = viewModel()) {
    val viewState by viewModel.state.collectAsState()
    var addr = "알수 없음"
    viewModel.lastLocation.value?.let{
        val gCoder = Geocoder(LocalContext.current, Locale.KOREA)
        val locInfo = gCoder.getFromLocation(it.latitude, it.longitude, 1) ?: return@let
        addr = locInfo[0].thoroughfare
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .selectableGroup()
        .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
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
                text = addr
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.labelLarge,
                text = "${viewState.stationList.size}곳 충전가능"
            )
        }
        Spacer(modifier = Modifier.height(
            dimensionResource(id = R.dimen.layout_3)
        ))
        Box(modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer)) {
            Image(
                painter = painterResource(id = R.drawable.icon_refresh),
                contentDescription = null
            )
        }
    }
}
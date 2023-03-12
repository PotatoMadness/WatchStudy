package com.example.testevinfra.presentation.ui.main

import android.content.res.Configuration
import android.graphics.drawable.PaintDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.testevinfra.R

@Composable
fun LoadingScreen(onLoadDone: () -> Unit,
    viewModel: MainViewModel?) {
    val viewState = viewModel?.state?.collectAsState()
    if (viewState?.value?.refreshing != true) {
        onLoadDone.invoke()
    }
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(id = R.drawable.icon_logo),
            contentDescription = null)
    }
}


@Preview(
    apiLevel = 26,
    uiMode = Configuration.UI_MODE_TYPE_WATCH,
    showSystemUi = true,
    device = Devices.WEAR_OS_SMALL_ROUND
)
@Composable
fun loadingView() {
    Box {
        LoadingScreen(
            onLoadDone = {}, null)
    }
}

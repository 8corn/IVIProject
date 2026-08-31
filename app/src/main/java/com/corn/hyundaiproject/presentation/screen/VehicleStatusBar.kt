package com.corn.hyundaiproject.presentation.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VehicleStatusBar(
    outsideTemp: String = "20.5",
    userName: String = "Driver 1"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) { }
}
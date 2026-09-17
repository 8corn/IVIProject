package com.corn.hyundaiproject.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun VehicleStatusBar(
    outsideTemp: Float = 20.5f,
    userName: String = "Driver 1"
) {
    var currentTime by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
            currentTime = sdf.format(Date())
            delay(1000)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color.Black)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "12:30 PM",
            color = Color.White,
            fontSize = 14.sp
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${outsideTemp}°C",
                color = Color.White,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.width(16.dp))

            Icon(
                imageVector = Icons.Default.Bluetooth,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = userName,
                color = Color.LightGray,
                fontSize = 14.sp
            )
        }
    }
}
package com.corn.hyundaiproject.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.corn.hyundaiproject.presentation.viewModel.CarViewModel

@Composable
fun SettingScreen(
    carViewModel: CarViewModel,
    onBackClick: () -> Unit
) {
    val details by carViewModel.vehicleDetails.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = "차량 설정",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 32.dp)
            )
        }

        SettingItem(
            title = "모델명",
            value = details["model"] ?: "G70 Sport"
        )

        SettingItem(
            title = "차대번호 (VIN)",
            value = details["vin"] ?: "KMH-G70-2026-XXXX"
        )

        SettingItem(
            title = "엔진 상태",
            value = "${details["engine_temp"] ?: "0"}°C (정상)"
        )

        SettingItem(
            title = "드리이브 모드",
            value = details["drive_mode"] ?: "NORMAL"
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Software Version: 1.0.0-8corn",
            color = Color.DarkGray,
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }
}
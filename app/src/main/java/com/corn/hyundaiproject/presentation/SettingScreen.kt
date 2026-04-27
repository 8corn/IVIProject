package com.corn.hyundaiproject.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.corn.hyundaiproject.presentation.ui.theme.HyundaiProjectTheme
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
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
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

            Spacer(modifier = Modifier.width(20.dp))

            Text(
                text = "차량 설정",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column (
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

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

            repeat(10) {
                SettingItem(
                    title = "추가 설정 항목 $it",
                    value = "설정값"
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Software Version: 1.0.0-8corn",
                color = Color.DarkGray,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
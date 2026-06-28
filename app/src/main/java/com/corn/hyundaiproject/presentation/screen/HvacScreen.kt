package com.corn.hyundaiproject.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.corn.hyundaiproject.presentation.ui.theme.HyundaiPureBlack
import com.corn.hyundaiproject.presentation.ui.theme.MetallicSilver
import com.corn.hyundaiproject.presentation.viewModel.CarViewModel

@Composable
fun HvacScreen(
    carViewModel: CarViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val hvacState by carViewModel.hvacInfo.collectAsState()

    val currentTemp = hvacState?.temperature ?: 22.0f
    val warningMessage = hvacState?.warningMessage ?: "실내 온도가 쾌적한 상태입니다."
    val isLocked = hvacState?.isDoorLocked ?: false

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HyundaiPureBlack)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MetallicSilver
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Text(
                text = "공조 시스템 설정 (HVAC)",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = if (isLocked) "차량 문 잠김" else "차량 문 열림",
                color = if (isLocked) Color.Gray else Color.Yellow,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(end = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}
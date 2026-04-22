package com.corn.hyundaiproject.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.corn.hyundaiproject.presentation.ui.theme.CarbonBlack
import com.corn.hyundaiproject.presentation.ui.theme.DeepGray
import com.corn.hyundaiproject.presentation.ui.theme.G70Red
import com.corn.hyundaiproject.presentation.viewModel.CarViewModel
import com.corn.hyundaiproject.presentation.viewModel.MainViewModel

@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    carViewModel: CarViewModel,
) {
    val hvacState by mainViewModel.hvacState.collectAsState()
    val drivingStatus by mainViewModel.drivingStatus.collectAsState()
    val climateAdvice by mainViewModel.climateAdvice.collectAsState()
    val details by mainViewModel.vehicleDetails.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(0.65f)
                .fillMaxHeight()
                .padding(end = 8.dp)
                .background(DeepGray, RoundedCornerShape(28.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "G70 Sport",
                    color = G70Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black
                )

                val currentRpm = details["rpm"]?.toIntOrNull() ?: 0

                Row {
                    Text(
                        text = "${details["speed"] ?: 0} MPH",
                        color = Color.White,
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Text(
                        text = "$currentRpm RPM",
                        color = if (currentRpm >= 7000) G70Red else Color.White,
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "현재 모드: ${details["drive_mode"] ?: "알 수 없음"}",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "엔진온도: ${details["engine_temp"] ?: "0"}°C",
                    color = Color.LightGray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = drivingStatus,
                    color = if (drivingStatus.contains("위험")) G70Red else Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = climateAdvice,
                    color = if (climateAdvice.contains("경고")) G70Red else Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column (
            modifier = Modifier
                .weight(0.35f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            hvacState.warningMessage?.let { message ->
                Text(
                    text = message,
                    color = G70Red,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
            }

            HvacWidget(
                hvacState.temperature,
                onIncrease = {
                    mainViewModel.updateTemperature(0.5f)
                },
                onDecrease = {
                    mainViewModel.updateTemperature(-0.5f)
                }
            )

            ControlWidget(
                isLocked = hvacState.isDoorLocked,
                onLockClick = {
                    mainViewModel.toggleDoorLock()
                },
                onWindowClick = {
                    mainViewModel.toggleWindow()
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(CarbonBlack, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Media Player",
                    color = Color.DarkGray
                )
            }
        }
    }
}
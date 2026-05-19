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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.corn.hyundaiproject.presentation.ui.theme.CarbonBlack
import com.corn.hyundaiproject.presentation.ui.theme.G70Red
import com.corn.hyundaiproject.presentation.viewModel.MainViewModel
import com.corn.hyundaiproject.presentation.viewModel.MediaViewModel
import java.util.Locale

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    mediaViewModel: MediaViewModel = hiltViewModel(),
    onSettingsClick: () -> Unit
) {
    val uiState by mainViewModel.uiState.collectAsState()
    val mediaState by mediaViewModel.mediaState.collectAsState()

    val speed = uiState.speed.toIntOrNull() ?: 0
    val rpm = uiState.rpm
    val currentMode = uiState.drivingStatus

    if (uiState.showFuelDialog) {
        AlertDialog(
            onDismissRequest = { mainViewModel.dismissFuelDialog() },
            title = {
                Text(
                    text = "연료 부족 경고",
                    fontWeight = FontWeight.Bold,
                    color = G70Red
                )
            },
            text = {
                Text(
                    text = "현재 연료가 ${String.format(Locale.getDefault(), "%.1f", uiState.fuelLevel)}% 남았습니다. 근처 가까운 현대오일뱅크(1.2km)로 경로를 안내할까요?",
                )
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = G70Red),
                    onClick = {
                        mainViewModel.startGasStationNavigation()
                    }
                ) {
                    Text(
                        text = "주유소 검색 및 안내"
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mainViewModel.dismissFuelDialog()
                    }
                ) {
                    Text(
                        text = "닫기",
                        color = Color.Gray
                    )
                }
            }
        )
    }

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
                .background(CarbonBlack, RoundedCornerShape(28.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isNavigatingToGasStation) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Navigation Mode",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "현대 오일뱅크 서초지점 안내 중",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        text = "남은 거리: 1.2Km | 예상 소요 시간: 4분",
                        color = G70Red,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "현재 속도: $speed km/h | 연료량: ${String.format(Locale.getDefault(), "%.1f", uiState.fuelLevel)}%",
                        color = Color.LightGray,
                        fontSize = 14.sp,
                    )
                }
            } else {
                DashboardWidget(
                    speed = speed,
                    rpm = rpm,
                    driveMode = currentMode,
                    isLaneDeparture = uiState.isLaneDeparture,
                    forwardDistance = uiState.forwardDistance,
                )
            }
        }

        Column (
            modifier = Modifier
                .weight(0.35f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (uiState.warningMessage.isNotEmpty()) {
                Text(
                    text = uiState.warningMessage,
                    color = G70Red,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
            }

            HvacWidget(
                uiState.temperature,
                onIncrease = {
                    mainViewModel.updateTemperature(0.5f)
                },
                onDecrease = {
                    mainViewModel.updateTemperature(-0.5f)
                }
            )

            ControlWidget(
                isLocked = uiState.isDoorLocked,
                isWindowOpen = uiState.isWindowOpen,
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
            ) {
                MediaWidget(
                    state = mediaState,
                    onPlayPause = { mediaViewModel.togglePlay() },
                    onSkipForward = { mediaViewModel.skipToNext() },
                    onSkipBackward = { mediaViewModel.skipToPrepare() },
                    onSettingsClick = onSettingsClick
                )
            }
        }
    }
}
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
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
import com.corn.hyundaiproject.presentation.ui.theme.CarbonBlack
import com.corn.hyundaiproject.presentation.ui.theme.DeepGray
import com.corn.hyundaiproject.presentation.ui.theme.G70Red
import com.corn.hyundaiproject.presentation.viewModel.CarViewModel
import com.corn.hyundaiproject.presentation.viewModel.MainViewModel
import com.corn.hyundaiproject.presentation.viewModel.MediaViewModel

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    mediaViewModel: MediaViewModel = hiltViewModel(),
    onSettingsClick: () -> Unit
) {
    val uiState by mainViewModel.uiState.collectAsState()
    val mediaState by mediaViewModel.mediaState.collectAsState()

    val speed = uiState.speed.toIntOrNull() ?: 0
    val rpm = 0f
    val currentMode = uiState.drivingStatus

    //    val drivingStatus by mainViewModel.drivingStatus.collectAsState()
    //    val climateAdvice by mainViewModel.climateAdvice.collectAsState()


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
            DashboardWidget(
                speed = speed,
                rpm = rpm,
                driveMode = currentMode,
                isLaneDeparture = uiState.isLaneDeparture,
                forwardDistance = uiState.forwardDistance,
            )
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
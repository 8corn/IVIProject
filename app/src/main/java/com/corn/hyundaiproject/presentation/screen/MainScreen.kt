package com.corn.hyundaiproject.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.corn.hyundaiproject.presentation.ControlWidget
import com.corn.hyundaiproject.presentation.DashboardWidget
import com.corn.hyundaiproject.presentation.HvacWidget
import com.corn.hyundaiproject.presentation.MediaWidget
import com.corn.hyundaiproject.presentation.ui.theme.CarbonBlack
import com.corn.hyundaiproject.presentation.ui.theme.G70Red
import com.corn.hyundaiproject.presentation.viewModel.MainViewModel
import com.corn.hyundaiproject.presentation.viewModel.MediaViewModel
import java.util.Locale

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    mediaViewModel: MediaViewModel = hiltViewModel(),
    onSettingsClick: () -> Unit,
    onHomeClick: () -> Unit,
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
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFF222222),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Canvas(
                            modifier = Modifier
                                .size(40.dp)
                        ) {
                            val path = Path().apply {
                                moveTo(size.width * 0.3f, size.height * 0.8f)
                                lineTo(size.width * 0.3f, size.height * 0.4f)
                                quadraticTo(size.width * 0.3f, size.height * 0.2f, size.width * 0.6f, size.height * 0.2f)
                                moveTo(size.width * 0.5f, size.height * 0.05f)
                                lineTo(size.width * 0.75f, size.height * 0.2f)
                                lineTo(size.width * 0.5f, size.height * 0.35f)
                            }
                            drawPath(
                                path = path,
                                color = G70Red,
                                style = Stroke(width = 6.dp.toPx())
                            )
                        }

                        Column {
                            Text(
                                text = "300m 앞 오른쪽 방향",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "현대오일뱅크 서초지점",
                                color = Color.LightGray,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            val w = size.width
                            val h = size.height

                            val horizonY = h * 0.3f

                            drawLine(
                                color = Color(0xFF444444),
                                Offset(w * 0.45f, horizonY),
                                Offset(w * 0.1f, h),
                                strokeWidth = 4f
                            )

                            drawLine(
                                color = Color(0xFF444444),
                                Offset(w * 0.55f, horizonY),
                                Offset(w * 0.9f, h),
                                strokeWidth = 4f
                            )

                            val numDashes = 5
                            for (i in 0 until numDashes) {
                                val progress = ((i.toFloat() / numDashes) + (uiState.roadOffset / 100f)) % 1.0f

                                val currY = horizonY + (h - horizonY) * progress
                                val nextY = horizonY + (h - horizonY) * (progress + 0.08f)

                                if (nextY < h) {
                                    val startX = w * 0.5f - (w * 0.05f * progress)
                                    val endX = w * 0.5f - (w * 0.05f * (progress + 0.08f))

                                    drawLine(
                                        color = Color(0xFFFFA100),
                                        start = Offset(w * 0.5f, currY),
                                        end = Offset(w * 0.5f, nextY),
                                        strokeWidth = 4f + (10f * progress)
                                    )
                                }
                            }
                        }

                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = "$speed",
                                color = Color.White,
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Black,
                            )

                            Text(
                                text = "km/h",
                                color = Color.Gray,
                                fontSize = 12.sp,
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${String.format(Locale.getDefault(), "%,.0f", rpm)}RPM",
                                    color = if (rpm > 5500) G70Red else Color.Cyan,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "${uiState.engineTemp}°C",
                                    color = Color.LightGray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val distanceKm = uiState.remainDistance / 1000f
                        val formattedDistance = String.format(Locale.getDefault(), "%.2f km", distanceKm)

                        val remainingMinutes = (distanceKm * 3.3f).toInt().coerceAtLeast(1)

                        Text(
                            text = "남은 거리: $formattedDistance",
                            color = G70Red,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "예상 소요 시간: $remainingMinutes 분",
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Row(
                    modifier = Modifier
                        .background(Color.Black.copy(0.6f), RoundedCornerShape(12.dp))
                        .clickable { onHomeClick() }
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                    )

                    Text(
                        text = "홈",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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
                )
            }
        }
    }
}
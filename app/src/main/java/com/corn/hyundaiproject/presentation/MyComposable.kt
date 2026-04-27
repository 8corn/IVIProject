package com.corn.hyundaiproject.presentation

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PauseCircleFilled
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.corn.hyundaiproject.presentation.ui.theme.CarbonBlack
import com.corn.hyundaiproject.presentation.ui.theme.DeepGray
import com.corn.hyundaiproject.presentation.ui.theme.G70Red
import com.corn.hyundaiproject.presentation.viewModel.MediaState
import java.util.Locale

@Composable
fun HvacWidget(
    temperature: Float,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CarbonBlack)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Climate",
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDecrease,
                    modifier = Modifier
                        .background(DeepGray, RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                Text(
                    text = String.format(Locale.getDefault(), "%.1f", temperature),
                    color = Color.White,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Light
                )

                IconButton(
                    onClick = onIncrease,
                    modifier = Modifier
                        .background(DeepGray, RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ControlWidget(
    isLocked: Boolean,
    onLockClick: (Boolean) -> Unit,
    onWindowClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CarbonBlack)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Text(
                text = "Quick Control",
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onWindowClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepGray)
                ) {
                    Text(
                        text = "창문",
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {
                        onLockClick(isLocked)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = G70Red)
                ) {
                    Icon(
                        imageVector = if (isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = if (!isLocked) "문 열림" else "문 닫힘",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun SettingItem(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = Color.LightGray,
            fontSize = 18.sp
        )

        Text(
            text = value,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun MediaWidget(
    state: MediaState,
    onPlayPause: () -> Unit,
    onSettingsClick: () -> Unit,
    onSkipForward: () -> Unit = {},
    onSkipBackward: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CarbonBlack, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp)
                    .height(270.dp)
                    .background(DeepGray, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = G70Red,
                    modifier = Modifier
                        .size(80.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
            ) {
                Text(
                    text = state.title,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = state.artist,
                    color = Color.Gray,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(3.dp))

                LinearProgressIndicator(
                    progress = {
                        state.progress
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = G70Red,
                    trackColor = DeepGray
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = state.currentTime,
                        color = Color.DarkGray,
                        fontSize = 12.sp
                    )

                    Text(
                        text = state.totalTime,
                        color = Color.DarkGray,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                IconButton(
                    onClick = onSkipBackward
                ) {
                    Icon(
                        imageVector = Icons.Default.Replay10,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(40.dp)
                    )
                }

                IconButton(
                    onClick = onPlayPause,
                    modifier = Modifier
                        .size(70.dp)
                ) {
                    Icon(
                        imageVector = if (state.isPlaying) Icons.Default.PauseCircleFilled else Icons.Default.PlayCircleFilled,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(64.dp)
                    )
                }

                IconButton(
                    onClick = onSkipForward
                ) {
                    Icon(
                        imageVector = Icons.Default.Forward10,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color.Gray,
                modifier = Modifier
                    .size(40.dp)
            )
        }
    }
}
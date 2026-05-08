package com.corn.hyundaiproject.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PauseCircleFilled
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.corn.hyundaiproject.R
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
                AnimatedContent(
                    targetState = state.title,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(500)) + scaleIn(initialScale = 0.92f))
                            .togetherWith(fadeOut(animationSpec = tween(500)))
                    },
                    label = "AlbumArtAnimation"
                ) { targetTitle ->
                    val artRes = getAlbumArt(targetTitle)

                    if (artRes != 0) {
                        Image(
                            painter = painterResource(id = artRes),
                            contentDescription = "Album Art",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            tint = G70Red,
                            modifier = Modifier
                                .size(80.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

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

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                IconButton(
                    onClick = onSkipBackward
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous Track",
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
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next Track",
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

@Composable
fun getAlbumArt(title: String): Int {
    return when (title) {
        "Bad Day" -> R.drawable.impmon
        "Congrats" -> R.drawable.omegamon
        "Gone, Gone, Gone" -> R.drawable.gilmon
        else -> 0
    }
}

@Composable
fun DashboardWidget(
    speed: Int,
    rpm: Float,
    driveMode: String = "NORMAL",
    isLaneDeparture: Boolean,
    forwardDistance: Float,
) {
    Row (
        modifier = Modifier
            .fillMaxSize()
            .background(CarbonBlack, RoundedCornerShape(28.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GaugeComponent(
            value = speed.toFloat(),
            maxValue = 260f,
            label = "Km/H",
            currentValueText = "$speed",
            gaugeColor = Color.White
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = driveMode,
                color = if (driveMode.contains("SPORT")) G70Red else Color.Cyan,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
            )

            Text(
                text = "MODE",
                color = Color.Gray,
                fontSize = 12.sp
            )

            AdasWidget(
                isLaneDeparture = isLaneDeparture,
                forwardDistance = forwardDistance,
                isObjectDetected = true
            )
        }

        GaugeComponent(
            value = rpm,
            maxValue = 9000f,
            label = "RPM",
            currentValueText = "${rpm.toInt()}",
            gaugeColor = if (rpm >= 7000) G70Red else Color.White
        )
    }
}

@Composable
fun GaugeComponent(
    value: Float,
    maxValue: Float,
    label: String,
    currentValueText: String,
    gaugeColor: Color
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(200.dp)
        ) {
            drawArc(
                color = Color.DarkGray,
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )

            val rpmSweep = (value / maxValue).coerceIn(0f, 1f) * 270f

            drawArc(
                color = gaugeColor,
                startAngle = 135f,
                sweepAngle = rpmSweep,
                useCenter = false,
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = currentValueText,
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = label,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun AdasWidget(
    isLaneDeparture: Boolean,
    forwardDistance: Float,
    isObjectDetected: Boolean
) {
    Box(
        modifier = Modifier
            .size(200.dp, 150.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val width = size.width
            val height = size.height

            val laneColor = if (isLaneDeparture) G70Red else Color.Gray
            val path = Path().apply {
                moveTo(width * 0.2f, height)
                lineTo(width * 0.45f, height * 0.4f)
                moveTo(width * 0.8f, height)
                lineTo(width * 0.55f, height * 0.4f)
            }

            drawPath(
                path = path,
                color = laneColor,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )

            if (isObjectDetected) {
                val distanceAlpha = (1f - (forwardDistance / 100f)).coerceIn(0.3f, 1f)
                drawRect(
                    color = if (forwardDistance < 20f) G70Red else Color.White.copy(alpha = distanceAlpha),
                    size = Size(width * 0.2f, height * 0.15f),
                    topLeft = Offset(width * 0.4f, height * 0.35f)
                )
            }
        }

        if (isObjectDetected) {
            Text(
                text = "${forwardDistance.toInt()}m",
                color = if (forwardDistance < 20f) G70Red else Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )
        }
    }
}
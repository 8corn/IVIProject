package com.corn.hyundaiproject.presentation.screen

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.corn.hyundaiproject.presentation.ui.theme.ActiveGenesisRed
import com.corn.hyundaiproject.presentation.ui.theme.HyundaiPureBlack
import com.corn.hyundaiproject.presentation.ui.theme.MetallicSilver

@Composable
fun VoiceMemoScreen(
    onBackClick: () -> Unit
) {
    var recordingState by remember { mutableStateOf("IDLE") }
    var memoTimer by remember { mutableIntStateOf(0) }

    val savedMemos = remember {
        listOf(
            Triple("음성 메모 003", "2026.06.07", "00:45"),
            Triple("음성 메모 002", "2026.05.28", "01:12"),
            Triple("음성 메모 001", "2026.05.15", "00:23")
        )
    }

    Box(
        modifier = Modifier.run {
            fillMaxSize()
                .background(HyundaiPureBlack)
                .padding(24.dp)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "◀ 뒤로가기",
                        color = MetallicSilver,
                        modifier = Modifier
                            .clickable { onBackClick() },
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.width(24.dp))

                    Text(
                        text = "음성 메모",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = MetallicSilver,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onBackClick() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                ) {
                    Text(
                        text = "저장된 메모 목록 (${savedMemos.size}",
                        color = MetallicSilver,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(bottom = 12.dp)
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        savedMemos.forEach { memo ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF111214), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 20.dp, vertical = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column{
                                    Text(
                                        text = memo.first,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = memo.second,
                                        color = Color.Gray,
                                        fontSize = 12.sp
                                    )
                                }

                                Text(
                                    text = memo.third,
                                    color = MetallicSilver,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                        .background(Color(0xFF141517), RoundedCornerShape(16.dp))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(48.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val barHeights = if (recordingState == "RECORDING") {
                            listOf(20, 45, 15, 60, 35, 55, 25, 40, 10, 50)
                        } else {
                            listOf(8, 8, 8, 8, 8, 8, 8, 8, 8, 8)
                        }

                        barHeights.forEach { heightPercentage ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(heightPercentage / 100f)
                                    .background(
                                        if (recordingState == "RECORDING") ActiveGenesisRed else Color(0xFF333333),
                                        RoundedCornerShape(4.dp)
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = if (recordingState == "RECORDING") "00:0${memoTimer}" else "00:00",
                        color = if (recordingState == "RECORDING") Color.White else Color.Gray,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black
                    )

                    Text(
                        text = when (recordingState) {
                            "RECORDING" -> "주행 중 안전을 위해 말하듯이 메모하세요."
                            "PAUSED" -> "녹음이 일시정지 되었습니다."
                            else -> "아래 버튼을 눌러 녹음을 시작하세요."
                        },
                        color = Color.Gray,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 32.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (recordingState == "IDLE") {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .background(ActiveGenesisRed, CircleShape)
                                    .clickable {
                                        recordingState = "RECORDING"
                                        memoTimer = 4 // 타이머 임시 가상 주입
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(Color.White, RoundedCornerShape(4.dp))
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(Color(0xFF222326),CircleShape)
                                    .clickable {
                                        recordingState = if (recordingState == "RECORDING") "PAUSE" else "RECORDING"
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (recordingState == "RECORDING") "정지" else "재개",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(Color.White, CircleShape)
                                    .clickable {
                                        recordingState = "IDLE"
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "저장",
                                    color = Color.Black,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
package com.corn.hyundaiproject.presentation.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.corn.hyundaiproject.presentation.ui.theme.ActiveGenesisRed
import com.corn.hyundaiproject.presentation.ui.theme.HyundaiPureBlack
import com.corn.hyundaiproject.presentation.ui.theme.MetallicSilver
import com.corn.hyundaiproject.presentation.viewModel.VoiceMemoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun VoiceMemoScreen(
    onBackClick: () -> Unit,
    viewModel: VoiceMemoViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val isRecording by viewModel.isRecording
    val isPlaying by viewModel.isPlaying
    val timerCounter by viewModel.recordingDuration
    val savedMemos = viewModel.savedMemos

    var hasAudioPermission by remember {
        mutableStateOf(
            context.checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasAudioPermission = isGranted
    }

    val fileDateFormatter = remember { SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault()) }

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

                    if (savedMemos.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "저장된 음성 메모가 없습니다.",
                                color = Color.DarkGray,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            items(savedMemos) { file ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFF111214), RoundedCornerShape(12.dp))
                                        .clickable {
                                            viewModel.playMemo(file)
                                        }
                                        .padding(horizontal = 20.dp, vertical = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column{
                                        Text(
                                            text = file.nameWithoutExtension,
                                            color = Color.White,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Text(
                                            text = fileDateFormatter.format(Date(file.lastModified())),
                                            color = Color.Gray,
                                            fontSize = 12.sp
                                        )
                                    }

                                    Text(
                                        text = "재생",
                                        color = ActiveGenesisRed,
                                        fontSize = 13.sp
                                    )
                                }
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
                        val barHeights = if (isRecording) {
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
                                        if (isRecording) ActiveGenesisRed else Color(0xFF333333),
                                        RoundedCornerShape(4.dp)
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    val minutes = String.format(Locale.getDefault(), "%02d", timerCounter / 60)
                    val seconds = String.format(Locale.getDefault(), "%02d", timerCounter % 60)

                    Text(
                        text = "$minutes:$seconds",
                        color = if (isRecording) Color.White else Color.Gray,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black
                    )

                    Text(
                        text = if (isRecording) "실제 오디오 녹음 중..." else if (isPlaying) "음성 메모 재생 중..." else "마이크를 터치해 녹음을 시작하세요",
                        color = if (isRecording) ActiveGenesisRed else Color.Gray,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 32.dp)
                    )

                    if (!isRecording) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(ActiveGenesisRed, CircleShape)
                                .clickable {
                                    if (hasAudioPermission) {
                                        viewModel.startRecording()
                                    } else {
                                        permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                                    }
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
                                .size(72.dp)
                                .background(Color.White, CircleShape)
                                .clickable {
                                    viewModel.stopRecording()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(HyundaiPureBlack, RoundedCornerShape(2.dp))
                            )
                        }
                    }

                    if (isPlaying) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "재생 중지",
                            color = Color.LightGray,
                            fontSize = 13.sp,
                            modifier = Modifier
                                .background(Color(0xFF222326), RoundedCornerShape(6.dp))
                                .clickable {
                                    viewModel.stopPlaying()
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}
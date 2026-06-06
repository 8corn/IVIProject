package com.corn.hyundaiproject.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.corn.hyundaiproject.presentation.ui.theme.ActiveGenesisRed
import com.corn.hyundaiproject.presentation.ui.theme.HyundaiPureBlack
import com.corn.hyundaiproject.presentation.ui.theme.MetallicSilver

@Composable
fun ProjectionScreen(
    onBackClick: () -> Unit
) {
    var isDeviceConnected by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HyundaiPureBlack)
            .padding(24.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

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
                        modifier = Modifier.clickable { onBackClick() },
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.width(24.dp))

                    Text(
                        text = "폰 프로젝션",
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
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.45f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(Color(0xFF111214), RoundedCornerShape(14.dp))
                            .border(
                                width = if (isDeviceConnected) 1.dp else 0.dp,
                                color = if (isDeviceConnected) Color(0xFF3B82F6) else Color.Transparent,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .padding(20.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Text(
                                text = "Android Auto",
                                color = if (isDeviceConnected) Color(0xFF3B82F6) else Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "안드로이드 스마트폰의 주요 기능을\n차량 디스플레이에서 확인하고 제어합니다.",
                                color = Color.Gray,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(Color(0xFF111214), RoundedCornerShape(14.dp))
                            .padding(20.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Text(
                                text = "Apple CarPlay",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "iPhone의 지원되는 기능을 차량 시스템과\n연동하여 안전하게 사용합니다.",
                                color = Color.Gray,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(0.55f)
                        .fillMaxHeight()
                        .background(Color(0xFF141517), RoundedCornerShape(16.dp))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (!isDeviceConnected) {
                        Text(
                            text = "연결된 기기가 없습니다.",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "정품 USB 케이블을 사용하여 스마트폰을\n차량의 전면 USB 단자에 연결해 주세요.",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Box(
                            modifier = Modifier
                                .background(Color(0xFF222326), RoundedCornerShape(8.dp))
                                .clickable { isDeviceConnected = true }
                                .padding(horizontal = 24.dp, vertical = 12.dp)
                        ) {
                            Text(text = "가상 USB 케이블 연결하기", color = MetallicSilver, fontSize = 14.sp)
                        }
                    } else {
                        Text(
                            text = "폰 프로젝션 준비 완료",
                            color = Color(0xFF3B82F6),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Android 폰이 정상 인식되었습니다.",
                            color = Color.LightGray,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .background(ActiveGenesisRed, RoundedCornerShape(10.dp))
                                .clickable {
                                    // 상용 시스템에서는 인텐트를 날려 Android Auto Activity를 띄우는 구간
                                }
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Android Auto 시작",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "연결 해제하려면 아래 버튼을 누르세요.",
                            color = Color(0xFF444444),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "해제하기",
                            color = Color.Gray,
                            fontSize = 13.sp,
                            modifier = Modifier.clickable { isDeviceConnected = false }
                        )
                    }
                }
            }
        }
    }
}
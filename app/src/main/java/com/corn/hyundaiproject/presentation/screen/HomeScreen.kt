package com.corn.hyundaiproject.presentation.screen

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.corn.hyundaiproject.presentation.CustomCircleIcon
import com.corn.hyundaiproject.presentation.model.LauncherAppItem
import com.corn.hyundaiproject.presentation.ui.theme.ActiveGenesisRed
import com.corn.hyundaiproject.presentation.ui.theme.HyundaiPureBlack
import com.corn.hyundaiproject.presentation.ui.theme.MetallicSilver
import com.corn.hyundaiproject.presentation.ui.theme.PanelDividerColor

@Composable
fun HomeScreen(
    onAppClick: (String) -> Unit
) {
    val appList = remember {
        listOf(
            LauncherAppItem("지도", "Tverskaya ulitsa,\nMoskva, 러시아", "map_screen") { m, c -> CustomCircleIcon(m, c, true) },
            LauncherAppItem("검색/경로", "경로 설정", "search_screen") { m, c -> CustomCircleIcon(m, c, false) },
            LauncherAppItem("라디오", "87.5\nБизнесFM", "radio_screen") { m, c -> CustomCircleIcon(m, c, false) },
            LauncherAppItem("미디어", "연결된 기기가\n없습니다.", "media_screen") { m, c -> CustomCircleIcon(m, c, false) },
            LauncherAppItem("전화", "연결된 기기가\n없습니다.", "phone_screen") { m, c -> CustomCircleIcon(m, c, false) },
            LauncherAppItem("폰 프로젝션", "연결된 기기가\n없습니다.", "projection_screen") { m, c -> CustomCircleIcon(m, c, false) },
            LauncherAppItem("음성 메모", "메모 0건", "voice_screen") { m, c -> CustomCircleIcon(m, c, false) },
            LauncherAppItem("설정", "게스트\n사용 중", "setting_screen") { m, c -> CustomCircleIcon(m, c, false) }
        )
    }

    var selectedIndex by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HyundaiPureBlack)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .align(Alignment.BottomCenter)
        ) {
            val w = size.width
            val h = size.height
            val path = Path().apply {
                moveTo(0f, h)
                lineTo(0f, h * 0.6f)
                // 가상 빌딩 숲 실루엣 그리기
                lineTo(w * 0.1f, h * 0.55f)
                lineTo(w * 0.12f, h * 0.4f)
                lineTo(w * 0.15f, h * 0.4f)
                lineTo(w * 0.17f, h * 0.65f)
                lineTo(w * 0.3f, h * 0.58f)
                lineTo(w * 0.33f, h * 0.35f)
                lineTo(w * 0.38f, h * 0.35f)
                lineTo(w * 0.42f, h * 0.62f)
                lineTo(w * 0.6f, h * 0.5f)
                lineTo(w * 0.65f, h * 0.45f)
                lineTo(w * 0.7f, h * 0.68f)
                lineTo(w * 0.85f, h * 0.52f)
                lineTo(w * 0.88f, h * 0.38f)
                lineTo(w * 0.92f, h * 0.38f)
                lineTo(w, h * 0.6f)
                lineTo(w, h)
                close()
            }
            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF15161A), Color(0xFF070708)),
                    startY = 0f,
                    endY = h
                )
            )
            drawLine(
                color = Color(0xFF25262B),
                start = Offset(0f, h * 0.6f),
                end = Offset(w, h * 0.6f),
                strokeWidth = 2f
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(Color(0xFF0A0A0B))
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF2A3A50), CircleShape)
                    .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = MetallicSilver,
                    modifier = Modifier.size(28.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { /* 메뉴 옵션 */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = MetallicSilver,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "메뉴", color = MetallicSilver, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 64.dp)
        ) {
            appList.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            if (isSelected) {
                                Brush.verticalGradient(
                                    colors = listOf(ActiveGenesisRed.copy(alpha = 0.3f), Color(0xFF080406))
                                )
                            } else {
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Transparent)
                                )
                            }
                        )
                        .clickable {
                            selectedIndex = index
                            onAppClick(item.route)
                        }
                        .padding(vertical = 40.dp, horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    item.drawIcon(
                        Modifier.size(72.dp),
                        if (isSelected) Color.White else MetallicSilver
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = item.title,
                            color = if (isSelected) Color(0xFFD63346) else MetallicSilver, // 활성화 시 인포레드 적용
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = item.subText,
                            color = if (isSelected) Color.Gray else Color(0xFF555555),
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.height(32.dp)
                        )
                    }
                }

                if (index < appList.lastIndex) {
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        PanelDividerColor,
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }
            }
        }
    }
}
package com.corn.hyundaiproject.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.corn.hyundaiproject.presentation.ui.theme.ActiveGenesisRed
import com.corn.hyundaiproject.presentation.ui.theme.HyundaiPureBlack
import com.corn.hyundaiproject.presentation.ui.theme.MetallicSilver
import com.corn.hyundaiproject.presentation.viewModel.RadioViewModel
import java.util.Locale

@Composable
fun RadioScreen(
    onBackClick: () -> Unit,
    viewModel: RadioViewModel = hiltViewModel()
) {
    val frequency by viewModel.currentFrequency.collectAsState()
    val station by viewModel.stationName.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HyundaiPureBlack)
            .padding(32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "◀ 뒤로가기",
                color = MetallicSilver,
                modifier = Modifier
                    .clickable {
                        onBackClick()
                    },
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.width(24.dp))

            Text(
                text = "라디오",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = station,
                color = ActiveGenesisRed,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = String.format(Locale.getDefault(), "%.1f MHz", frequency),
                color = Color.White,
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Button (
                    onClick = { viewModel.tuneDown() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222225)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "TUNE -",
                        color = MetallicSilver,
                        fontSize = 18.sp
                    )
                }

                Button(
                    onClick = { viewModel.tuneUp() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222225)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "TUNE +",
                        color = MetallicSilver,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
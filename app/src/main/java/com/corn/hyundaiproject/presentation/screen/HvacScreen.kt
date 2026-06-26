package com.corn.hyundaiproject.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.corn.hyundaiproject.presentation.ui.theme.HyundaiPureBlack
import com.corn.hyundaiproject.presentation.viewModel.CarViewModel

@Composable
fun HvacScreen(
    carViewModel: CarViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val hvacState by carViewModel.hvacInfo.collectAsState()

    val currentTemp = hvacState?.temperature ?: 22.0f
    val warningMessage = hvacState?.warningMessage ?: "실내 온도가 쾌적한 상태입니다."
    val isLocked = hvacState?.isDoorLocked ?: false

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HyundaiPureBlack)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) { }
    }
}
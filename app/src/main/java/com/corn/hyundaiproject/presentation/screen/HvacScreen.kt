package com.corn.hyundaiproject.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
}
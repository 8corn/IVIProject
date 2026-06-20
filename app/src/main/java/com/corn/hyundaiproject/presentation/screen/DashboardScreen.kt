package com.corn.hyundaiproject.presentation.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.corn.hyundaiproject.presentation.viewModel.CarViewModel

@Composable
fun DashboardScreen(
    carViewModel: CarViewModel = hiltViewModel(),
    onNavigateToSettings: () -> Unit,
    onNavigateToHvac: () -> Unit
) {
    val details by carViewModel.vehicleDetails.collectAsState()

    IconButton(
        onClick = onNavigateToSettings
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "설정 이동"
        )
    }
}
package com.corn.hyundaiproject.presentation.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun DashboardScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToHvac: () -> Unit
) {
    IconButton(
        onClick = onNavigateToSettings
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "설정 이동"
        )
    }
}
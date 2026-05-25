package com.corn.hyundaiproject.presentation.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

data class LauncherAppItem(
    val title: String,
    val subText: String,
    val route: String,
    val drawIcon: @Composable (modifier: Modifier, color: Color) -> Unit
)
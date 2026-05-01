package com.corn.hyundaiproject.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.corn.hyundaiproject.presentation.ui.theme.HyundaiProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var currentScreen by remember { mutableStateOf("main") }

            HyundaiProjectTheme {
                when (currentScreen) {
                    "main" -> MainScreen(
                        onSettingsClick = { currentScreen = "settings" }
                    )
                    "settings" -> SettingScreen(
                        onBackClick = { currentScreen = "main" }
                    )
                }
            }
        }
    }
}
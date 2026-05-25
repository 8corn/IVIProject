package com.corn.hyundaiproject.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.corn.hyundaiproject.presentation.screen.HomeScreen
import com.corn.hyundaiproject.presentation.screen.MainScreen
import com.corn.hyundaiproject.presentation.screen.SettingScreen
import com.corn.hyundaiproject.presentation.ui.theme.HyundaiProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var currentScreen by remember { mutableStateOf("launcher") }

            HyundaiProjectTheme {
                when (currentScreen) {
                    "launcher" -> HomeScreen(
                        onAppClick = { route ->
                            when (route) {
                                "map_screen" -> currentScreen = "main"
                                "setting_screen" -> currentScreen = "settings"
                                else -> {
                                    // Todo: 라디오, 미디어 등
                                }
                            }
                        }
                    )
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
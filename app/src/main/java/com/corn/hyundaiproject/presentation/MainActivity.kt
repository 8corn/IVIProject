package com.corn.hyundaiproject.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.corn.hyundaiproject.presentation.navigation.HyundaiNavHost
import com.corn.hyundaiproject.presentation.ui.theme.HyundaiProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HyundaiProjectTheme {
                val navController = rememberNavController()

                HyundaiNavHost(navController = navController)
            }
        }
    }
}
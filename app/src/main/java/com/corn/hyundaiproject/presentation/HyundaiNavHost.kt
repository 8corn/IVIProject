package com.corn.hyundaiproject.presentation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.corn.hyundaiproject.presentation.screen.HomeScreen
import com.corn.hyundaiproject.presentation.screen.MainScreen
import com.corn.hyundaiproject.presentation.screen.PhoneScreen
import com.corn.hyundaiproject.presentation.screen.ProjectionScreen
import com.corn.hyundaiproject.presentation.screen.RadioScreen
import com.corn.hyundaiproject.presentation.screen.SearchScreen
import com.corn.hyundaiproject.presentation.screen.SettingScreen
import com.corn.hyundaiproject.presentation.screen.VoiceMemoScreen

@Composable
fun HyundaiNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = "launcher",
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() },
    ) {
        composable ("launcher") {
            HomeScreen (
                onAppClick = { route ->
                    when(route) {
                        "map_screen" -> navController.navigate("main")
                        "search_screen" -> navController.navigate("search")
                        "radio_screen" -> navController.navigate("radio")
                        "phone_screen" -> navController.navigate("phone")
                        "projection_screen" -> navController.navigate("projection")
                        "voice_screen" -> navController.navigate("voice")
                        "setting_screen" -> navController.navigate("settings")
                        else -> {
                            // TODO: 다른 화면 대기
                        }
                    }
                }
            )
        }
        composable ("main") {
            MainScreen(
                onSettingsClick = {
                    navController.navigate("settings")
                },
                onHomeClick = {
                    navController.popBackStack(
                        route = "launcher",
                        inclusive = false
                    )
                }
            )
        }
        composable("search") {
            SearchScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onRouteSelect = { selectedDestination ->
                    navController.navigate("main") {
                        popUpTo("launcher")
                    }
                }
            )
        }
        composable("radio") {
            RadioScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable("phone") {
            PhoneScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable("projection") {
            ProjectionScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable("voice") {
            VoiceMemoScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable("settings") {
            SettingScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
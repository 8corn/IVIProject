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
import com.corn.hyundaiproject.presentation.screen.RadioScreen
import com.corn.hyundaiproject.presentation.screen.SearchScreen
import com.corn.hyundaiproject.presentation.screen.SettingScreen

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
        composable("settings") {
            SettingScreen(
                onBackClick = {
                    navController.popBackStack()
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
    }
}
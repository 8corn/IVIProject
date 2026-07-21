package com.corn.hyundaiproject.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.corn.hyundaiproject.presentation.screen.DashboardScreen
import com.corn.hyundaiproject.presentation.screen.HvacScreen
import com.corn.hyundaiproject.presentation.screen.SettingScreen

@Composable
fun VehicleNavGraph(
    navController: NavHostController = rememberNavController(),
    onHomeClick: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = VehicleRoutes.DASHBOARD
    ) {
        composable (route = VehicleRoutes.DASHBOARD) {
            DashboardScreen(
                onNavigateToSettings = {
                    navController.navigate(VehicleRoutes.SETTINGS)
                },
                onNavigateToHvac = {
                    navController.navigate(VehicleRoutes.HVAC)
                },
                onHomeClick = onHomeClick
            )
        }

        composable (route = VehicleRoutes.SETTINGS) {
            SettingScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable (route = VehicleRoutes.HVAC) {
            HvacScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
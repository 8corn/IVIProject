package com.corn.hyundaiproject.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.corn.hyundaiproject.presentation.screen.SettingScreen

@Composable
fun VehicleNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = VehicleRoutes.DASHBOARD
    ) {
        composable (route = VehicleRoutes.DASHBOARD) {
            // 메인 대시보드 화면 주소 등록
        }

        composable (route = VehicleRoutes.SETTINGS) {
            SettingScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable (route = VehicleRoutes.HVAC) {
            // 공조 제어 화면 주소 등록
        }
    }
}
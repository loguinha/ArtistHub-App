package com.example.artisthub.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.artisthub.ui.screens.HomeScreen
import com.example.artisthub.ui.screens.StoreScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("store/{storeId}") { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId") ?: ""
            StoreScreen(
                storeId = storeId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
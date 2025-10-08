package com.example.scrollbooker.navigation.host

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.scrollbooker.navigation.graphs.calendarGraph
import com.example.scrollbooker.navigation.graphs.globalGraph
import com.example.scrollbooker.navigation.routes.GlobalRoute
import com.example.scrollbooker.ui.auth.AuthViewModel

@Composable
fun MainNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = GlobalRoute.MAIN
    ) {
        composable(GlobalRoute.MAIN) {
            MainApplication(authViewModel = authViewModel)
        }

        // Global Routes
        globalGraph(navController = navController)
        calendarGraph(navController = navController)
    }
}
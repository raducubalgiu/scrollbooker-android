package com.example.scrollbooker.navigation.host
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.scrollbooker.navigation.graphs.calendarGraph
import com.example.scrollbooker.navigation.graphs.cameraGraph
import com.example.scrollbooker.navigation.graphs.globalGraph
import com.example.scrollbooker.navigation.routes.GlobalRoute

@Composable
fun MainNavHost(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = GlobalRoute.MAIN
    ) {
        composable(GlobalRoute.MAIN) {
            MainApplication(onLogout)
        }

        // Global Routes
        globalGraph(navController)
        calendarGraph(navController)
        cameraGraph(navController)
    }
}
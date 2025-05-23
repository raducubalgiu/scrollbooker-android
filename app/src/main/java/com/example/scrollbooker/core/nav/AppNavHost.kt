package com.example.scrollbooker.core.nav
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.scrollbooker.core.nav.graphs.authGraph
import com.example.scrollbooker.core.nav.graphs.mainGraph
import com.example.scrollbooker.core.nav.routes.GlobalRoute

@Composable
fun AppNavHost(
    startDestination: String,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        route = GlobalRoute.ROOT
    ) {
        authGraph(navController)
        mainGraph(navController)
    }
}


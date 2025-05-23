package com.example.scrollbooker.core.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scrollbooker.MainViewModel
import androidx.compose.runtime.getValue
import com.example.scrollbooker.core.nav.host.AuthNavHost
import com.example.scrollbooker.core.nav.host.MainNavHost
import com.example.scrollbooker.core.nav.routes.GlobalRoute

@Composable
fun RootNavHost() {
    val navController = rememberNavController()
    val viewModel: MainViewModel = hiltViewModel()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if(isLoggedIn) GlobalRoute.MAIN else GlobalRoute.AUTH
    ) {
        composable(GlobalRoute.AUTH) {
            AuthNavHost(navController, viewModel)
        }
        
        composable(GlobalRoute.MAIN) {
            MainNavHost(navController, viewModel)
        }
    }
}
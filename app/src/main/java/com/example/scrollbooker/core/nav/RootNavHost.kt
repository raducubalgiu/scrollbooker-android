package com.example.scrollbooker.core.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.scrollbooker.core.nav.host.AuthNavHost
import com.example.scrollbooker.core.nav.host.MainNavHost
import com.example.scrollbooker.core.nav.routes.GlobalRoute
import com.example.scrollbooker.feature.auth.presentation.AuthViewModel

@Composable
fun RootNavHost(navController: NavHostController) {
    val viewModel: AuthViewModel = hiltViewModel()
    var isInitilialized by remember { mutableStateOf(false) }
    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val loggedIn = viewModel.isLoggedIn()
        startDestination = if(loggedIn) GlobalRoute.MAIN else GlobalRoute.AUTH
        isInitilialized = true
    }

    if(!isInitilialized) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        NavHost(
            navController = navController,
            startDestination = startDestination!!
        ) {
            composable(GlobalRoute.AUTH) {
                AuthNavHost(viewModel)
            }

            composable(GlobalRoute.MAIN) {
                MainNavHost()
            }
        }
    }
}
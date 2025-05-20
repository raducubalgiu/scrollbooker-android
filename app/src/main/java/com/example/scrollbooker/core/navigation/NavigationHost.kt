package com.example.scrollbooker.core.navigation

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.scrollbooker.feature.appointments.presentation.AppointmentsScreen
import com.example.scrollbooker.feature.auth.presentation.AuthViewModel
import com.example.scrollbooker.feature.auth.presentation.LoginScreen
import com.example.scrollbooker.feature.auth.presentation.RegisterScreen
import com.example.scrollbooker.feature.feed.presentation.FeedScreen
import com.example.scrollbooker.feature.inbox.presentation.InboxScreen
import com.example.scrollbooker.feature.profile.presentation.ProfileScreen
import com.example.scrollbooker.feature.search.presentation.SearchScreen

@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    isLoggedIn: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = if(isLoggedIn) BottomNavItem.Feed.route else "login",
        modifier = modifier
    ) {
        composable("login") {
            val authViewModel: AuthViewModel = hiltViewModel()
            LoginScreen(
                navController = navController,
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(BottomNavItem.Feed.route) {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            val authViewModel: AuthViewModel = hiltViewModel()

            RegisterScreen(
                navController = navController,
                viewModel = authViewModel,
                onRegisterSuccess = {}
            )
        }

        composable(BottomNavItem.Feed.route) { FeedScreen() }
        composable(BottomNavItem.Inbox.route) { InboxScreen() }
        composable(BottomNavItem.Search.route) { SearchScreen() }
        composable(BottomNavItem.Appointments.route) { AppointmentsScreen() }
        composable(BottomNavItem.Profile.route) { ProfileScreen() }
    }
}
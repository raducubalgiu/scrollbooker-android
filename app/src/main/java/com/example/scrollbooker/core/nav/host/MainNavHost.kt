package com.example.scrollbooker.core.nav.host

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.scrollbooker.MainViewModel
import com.example.scrollbooker.core.nav.BottomBar
import com.example.scrollbooker.core.nav.graphs.profileGraph
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.feature.appointments.presentation.AppointmentsScreen
import com.example.scrollbooker.feature.feed.presentation.FeedScreen
import com.example.scrollbooker.feature.inbox.presentation.InboxScreen
import com.example.scrollbooker.feature.search.presentation.SearchScreen

@Composable
fun MainNavHost(viewModel: MainViewModel) {
    val bottomNavController = rememberNavController()
    val currentRoute = bottomNavController.currentBackStackEntryAsState().value?.destination?.route

    val containerColor = if(currentRoute == MainRoute.Feed.route) Color(0xFF121212) else MaterialTheme.colorScheme.background

    val bottomBarRoutes = setOf(
        MainRoute.Feed.route,
        MainRoute.Inbox.route,
        MainRoute.Search.route,
        MainRoute.Appointments.route,
        MainRoute.Profile.route
    )

    Scaffold(
        containerColor = containerColor,
        bottomBar = {
            if(currentRoute in bottomBarRoutes) {
                BottomBar(bottomNavController)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(containerColor)
            .padding(innerPadding)
        ) {
            NavHost(
                navController = bottomNavController,
                startDestination = MainRoute.Feed.route,
                enterTransition = {
                    fadeIn(animationSpec = tween(durationMillis = 100))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(durationMillis = 100))
                },
                popEnterTransition = {
                    fadeIn(animationSpec = tween(durationMillis = 100))
                },
                popExitTransition = {
                    fadeOut(animationSpec = tween(durationMillis = 100))
                },
            ) {
                composable(MainRoute.Feed.route) {
                    FeedScreen()
                }

                composable(MainRoute.Inbox.route) {
                    InboxScreen(
                        navController = bottomNavController
                    )
                }

                composable(MainRoute.Search.route) {
                    SearchScreen()
                }

                composable(MainRoute.Appointments.route) {
                    AppointmentsScreen(
                        navController = bottomNavController
                    )
                }

                profileGraph(navController = bottomNavController)
            }
        }
    }
}
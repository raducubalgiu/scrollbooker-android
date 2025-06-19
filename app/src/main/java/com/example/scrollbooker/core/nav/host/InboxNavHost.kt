package com.example.scrollbooker.core.nav.host

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.screens.inbox.InboxScreen
import com.example.scrollbooker.screens.inbox.InboxViewModel

@Composable
fun InboxNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.Inbox.route
    ) {
        composable(MainRoute.Inbox.route) { backStackEntry ->
            val viewModel = hiltViewModel<InboxViewModel>(backStackEntry)
            InboxScreen(viewModel = viewModel)
        }
    }
}
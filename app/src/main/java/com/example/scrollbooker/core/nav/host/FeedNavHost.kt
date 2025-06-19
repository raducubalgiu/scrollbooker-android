package com.example.scrollbooker.core.nav.host
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.screens.feed.FeedScreen
import com.example.scrollbooker.screens.feed.FeedViewModel

@Composable
fun FeedNavHost(
    navController: NavHostController,
    onOpenDrawer: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.Feed.route
    ) {
        composable(MainRoute.Feed.route) { backStackEntry ->
            val viewModel = hiltViewModel<FeedViewModel>(backStackEntry)
            FeedScreen(
                viewModel = viewModel,
                onOpenDrawer = onOpenDrawer
            )
        }
    }
}
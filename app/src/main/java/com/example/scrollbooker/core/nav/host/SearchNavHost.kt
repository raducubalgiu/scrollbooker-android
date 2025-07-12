package com.example.scrollbooker.core.nav.host

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.screens.search.businessProfile.BusinessProfileScreen
import com.example.scrollbooker.screens.search.SearchScreen
import com.example.scrollbooker.screens.search.SearchViewModel

@Composable
fun SearchNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.Search.route
    ) {
        composable(MainRoute.Search.route) { backStackEntry ->
            val viewModel = hiltViewModel<SearchViewModel>(backStackEntry)
            SearchScreen(
                viewModel = viewModel,
                onNavigateToBusinessProfile = { navController.navigate(MainRoute.BusinessProfile.route) }
            )
        }
        composable(MainRoute.BusinessProfile.route) { backStackEntry ->

            BusinessProfileScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
package com.example.scrollbooker.navigation.host
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.search.SearchScreen
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.search.businessProfile.BusinessProfileScreen
import androidx.compose.runtime.getValue

@Composable
fun SearchNavHost(navController: NavHostController) {
    Box(Modifier.fillMaxSize()) {
        val viewModel = hiltViewModel<SearchViewModel>()

        val backStackEntry by navController.currentBackStackEntryAsState()
        val route = backStackEntry?.destination?.route

        val isOnBusinessProfile = route == MainRoute.BusinessProfile.route

        SearchScreen(
            viewModel = viewModel,
            onNavigateToBusinessProfile = {
                viewModel.openBusinessProfile(it)
                navController.navigate(MainRoute.BusinessProfile.route)
            }
        )

        AnimatedContent(
            targetState = isOnBusinessProfile,
            transitionSpec = {
                if (targetState) {
                    slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                } else {
                    slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
                }
            },
            label = "search_to_profile"
        ) { showProfile ->
            if (showProfile) {
                BusinessProfileScreen(
                    viewModel = viewModel,
                    onBack = {
                        viewModel.closeBusinessProfile()
                        navController.popBackStack()
                    }
                )
            } else {
                Box(Modifier.fillMaxSize())
            }
        }

        NavHost(
            navController = navController,
            startDestination = MainRoute.Search.route
        ) {
            composable(MainRoute.Search.route) { }
            composable(MainRoute.BusinessProfile.route) {}
        }
    }
}
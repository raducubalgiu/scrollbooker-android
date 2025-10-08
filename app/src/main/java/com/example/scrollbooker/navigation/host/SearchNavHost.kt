package com.example.scrollbooker.navigation.host
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.search.SearchScreen
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.search.businessProfile.BusinessProfileScreen

@Composable
fun SearchNavHost(
    navController: NavHostController,
    appointmentsNumber: Int,
    notificationsNumber: Int,
) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.Search.route,
        enterTransition = { slideInFromRight() },
        exitTransition = { slideOutToLeft() },
        popEnterTransition = { slideInFromLeft() },
        popExitTransition = { slideOutToRight() }
    ) {
        composable(MainRoute.Search.route) { backStackEntry ->
            val viewModel = hiltViewModel<SearchViewModel>(backStackEntry)
            SearchScreen(
                viewModel = viewModel,
                onNavigateToBusinessProfile = { navController.navigate(MainRoute.BusinessProfile.route) },
                appointmentsNumber = appointmentsNumber,
                notificationsNumber = notificationsNumber,
            )
        }
        composable(MainRoute.BusinessProfile.route) { backStackEntry ->
            BusinessProfileScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
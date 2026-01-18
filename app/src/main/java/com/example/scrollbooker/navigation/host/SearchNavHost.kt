package com.example.scrollbooker.navigation.host
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.LocalMainNavController
import com.example.scrollbooker.ui.search.businessProfile.BusinessProfileScreen
import com.example.scrollbooker.ui.search.businessProfile.BusinessProfileViewModel

@Composable
fun SearchNavHost(
    navController: NavHostController
) {
    val mainNavController = LocalMainNavController.current

    NavHost(
        navController = navController,
        startDestination = MainRoute.Search.route,
        enterTransition = { slideInFromRight() },
        exitTransition = { slideOutToLeft() },
        popEnterTransition = { slideInFromLeft() },
        popExitTransition = { slideOutToRight() }
    ) {
        composable(MainRoute.Search.route) {
            Box(Modifier.fillMaxSize())
        }
        composable(
            route = MainRoute.BusinessProfile.route,
            arguments = listOf(
                navArgument(MainRoute.BusinessProfile.ARG_BUSINESS_ID) { type = NavType.IntType }
            ),
        ) {
            val viewModel: BusinessProfileViewModel = hiltViewModel()

            BusinessProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToUserProfile = {
                    mainNavController.navigate("${MainRoute.UserProfile.route}/$it")
                }
            )
        }
    }
}
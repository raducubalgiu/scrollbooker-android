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
import com.example.scrollbooker.navigation.graphs.userProfileGraph
import com.example.scrollbooker.navigation.navigators.NavigateSocialParam
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.search.businessProfile.BusinessProfileScreen
import com.example.scrollbooker.ui.search.businessProfile.BusinessProfileViewModel
import com.example.scrollbooker.ui.social.SocialScreen
import com.example.scrollbooker.ui.social.SocialViewModel

@Composable
fun SearchNavHost(
    navController: NavHostController
) {
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
                    navController.navigate("${MainRoute.UserProfile.route}/$it")
                }
            )
        }

        userProfileGraph(navController)

        composable(
            route = "${MainRoute.Social.route}/{tabIndex}/{userId}/{username}/{isBusinessOrEmployee}",
            arguments = listOf(
                navArgument("tabIndex") { type = NavType.IntType },
                navArgument("userId") { type = NavType.IntType },
                navArgument("username") { type = NavType.StringType },
                navArgument("isBusinessOrEmployee") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val tabIndex = backStackEntry.arguments?.getInt("tabIndex") ?: return@composable
            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
            val username = backStackEntry.arguments?.getString("username") ?: return@composable
            val isBusinessOrEmployee = backStackEntry.arguments?.getBoolean("isBusinessOrEmployee") ?: return@composable

            val viewModel = hiltViewModel<SocialViewModel>(backStackEntry)
            val socialParams = NavigateSocialParam(tabIndex, userId, username, isBusinessOrEmployee)

            SocialScreen(
                viewModal = viewModel,
                socialParam = socialParams,
                onBack = { navController.popBackStack() },
                onNavigateUserProfile = {
                    navController.navigate("${MainRoute.UserProfile.route}/$it") {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
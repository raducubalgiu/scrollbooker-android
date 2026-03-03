package com.example.scrollbooker.navigation.graphs

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.profile.ProfileViewModel
import com.example.scrollbooker.ui.profile.UserProfilePostDetailScreen
import com.example.scrollbooker.ui.profile.UserProfileScreen

fun NavGraphBuilder.userProfileGraph(
    navController: NavHostController
) {
    navigation(
        route = MainRoute.ProfileNavigator.route,
        startDestination = "${MainRoute.UserProfile.route}/{userId}",
        enterTransition = { slideInFromRight() },
        exitTransition = { slideOutToLeft() },
        popEnterTransition = { slideInFromLeft() },
        popExitTransition = { slideOutToRight() }
    ) {
        composable("${MainRoute.UserProfile.route}/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val viewModel = hiltViewModel<ProfileViewModel>(backStackEntry)

            val profileNavigate = remember(navController) {
                ProfileNavigator(navController)
            }

            UserProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                profileNavigate = profileNavigate,
            )
        }

        composable(
            route = "${MainRoute.UserProfilePostDetail.route}/{postTab}/{postIndex}/{userId}",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None},
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            arguments = listOf(
                navArgument("postTab") { type = NavType.StringType },
                navArgument("postIndex") { type = NavType.IntType },
                navArgument("userId") { type = NavType.IntType }
            ),
        ) { backStackEntry ->
            val postTabKey = backStackEntry.arguments?.getString("postTab") ?: return@composable
            val postIndex = backStackEntry.arguments?.getInt("postIndex") ?: return@composable
            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable

            val parentBackStackEntry = remember(backStackEntry) {
                navController.getBackStackEntry("${MainRoute.UserProfile.route}/$userId")
            }
            val viewModel = hiltViewModel<ProfileViewModel>(parentBackStackEntry)

            val profileNavigate = remember(navController) {
                ProfileNavigator(navController)
            }

            UserProfilePostDetailScreen(
                postTabKey = postTabKey,
                postIndex = postIndex,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                profileNavigate = profileNavigate,
            )
        }
    }
}
package com.example.scrollbooker.navigation.graphs

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.scrollbooker.core.util.SHARE_BASE_URL
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.profile.ProfileViewModel
import com.example.scrollbooker.ui.profile.UserProfilePostDetailScreen
import com.example.scrollbooker.ui.profile.UserProfileScreen

fun NavGraphBuilder.userProfileGraph(
    navController: NavHostController,
    profileNavigate: ProfileNavigator
) {
    navigation(
        route = MainRoute.ProfileNavigator.route,
        startDestination = MainRoute.UserProfile.route
    ) {
        composable(
            route = MainRoute.UserProfile.route,
            exitTransition = {
                if (targetState.destination.route?.startsWith(MainRoute.UserProfilePostDetail.route) == true) {
                    ExitTransition.None
                } else {
                    null
                }
            },
            popEnterTransition = {
                if (initialState.destination.route?.startsWith(MainRoute.UserProfilePostDetail.route) == true) {
                    EnterTransition.None
                } else {
                    null
                }
            },
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
                navArgument("username") { type = NavType.StringType },
                navArgument("profession") { type = NavType.StringType }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$SHARE_BASE_URL/user/{userId}/{username}/{profession}"
                }
            )
        ) { backStackEntry ->
            val viewModel = hiltViewModel<ProfileViewModel>(backStackEntry)

            UserProfileScreen(
                viewModel = viewModel,
                profileNavigate = profileNavigate,
            )
        }

        composable(
            route = MainRoute.UserProfilePostDetail.route,
            enterTransition = { fadeIn(animationSpec = tween(durationMillis = 200)) },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            arguments = listOf(
                navArgument("postTab") {
                    type = NavType.StringType
                },
                navArgument("postIndex") {
                    type = NavType.IntType
                },
                navArgument("userId") {
                    type = NavType.IntType
                }
            ),
        ) { backStackEntry ->
            val postTabKey = backStackEntry.arguments?.getString("postTab") ?: return@composable
            val postIndex = backStackEntry.arguments?.getInt("postIndex") ?: return@composable

            val parentBackStackEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.ProfileNavigator.route)
            }
            val viewModel = hiltViewModel<ProfileViewModel>(parentBackStackEntry)

            UserProfilePostDetailScreen(
                postTabKey = postTabKey,
                postIndex = postIndex,
                viewModel = viewModel,
                profileNavigate = profileNavigate
            )
        }
    }
}
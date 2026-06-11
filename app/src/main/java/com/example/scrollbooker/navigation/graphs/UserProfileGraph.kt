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
import com.example.scrollbooker.ui.profile.ProfileViewModel
import com.example.scrollbooker.ui.profile.UserProfilePostDetailScreen
import com.example.scrollbooker.ui.profile.UserProfileScreen

fun NavGraphBuilder.userProfileGraph(
    navController: NavHostController,
    profileNavigate: ProfileNavigator
) {
    navigation(
        route = MainRoute.ProfileNavigator.route,
        startDestination = "${MainRoute.UserProfile.route}/{userId}/{username}"
    ) {
        composable("${MainRoute.UserProfile.route}/{userId}/{username}",
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
                navArgument("username") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val viewModel = hiltViewModel<ProfileViewModel>(backStackEntry)

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
                onBack = { navController.popBackStack() },
                profileNavigate = profileNavigate
            )
        }
    }
}
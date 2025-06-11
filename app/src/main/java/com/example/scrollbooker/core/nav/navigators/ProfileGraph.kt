package com.example.scrollbooker.core.nav.navigators

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.feature.profile.presentation.ProfileSharedViewModel
import com.example.scrollbooker.feature.profile.presentation.UserProfileScreen
import com.example.scrollbooker.feature.profile.presentation.edit.EditBioScreen
import com.example.scrollbooker.feature.profile.presentation.edit.EditFullNameScreen
import com.example.scrollbooker.feature.profile.presentation.edit.EditGenderScreen
import com.example.scrollbooker.feature.profile.presentation.edit.EditProfileScreen
import com.example.scrollbooker.feature.profile.presentation.edit.EditUsernameScreen
import com.example.scrollbooker.feature.userSocial.presentation.UserSocialScreen
import com.example.scrollbooker.feature.userSocial.presentation.UserSocialViewModel

fun NavGraphBuilder.profileGraph(navController: NavHostController) {
    navigation(
        route = MainRoute.ProfileNavigator.route,
        startDestination = MainRoute.UserProfile.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(
                    durationMillis = 250,
                    easing = FastOutSlowInEasing
                )
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(
                    durationMillis = 250,
                    easing = FastOutSlowInEasing
                )
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(
                    durationMillis = 250,
                    easing = FastOutSlowInEasing
                )
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(
                    durationMillis = 250,
                    easing = FastOutSlowInEasing
                )
            )
        }
    ) {
        composable(MainRoute.UserProfile.route) { backStackEntry ->
            val viewModel = hiltViewModel<ProfileSharedViewModel>(backStackEntry)
            UserProfileScreen(
                viewModel = viewModel,
                onNavigate = { navController.navigate(it) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(MainRoute.EditProfile.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.MyProfile.route)
            }
            val viewModel = hiltViewModel<ProfileSharedViewModel>(parentEntry)

            EditProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigate = { route -> navController.navigate(route) }
            )
        }
        composable(MainRoute.EditFullName.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.MyProfile.route)
            }
            val viewModel: ProfileSharedViewModel = hiltViewModel(parentEntry)

            EditFullNameScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(MainRoute.EditUsername.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.MyProfile.route)
            }
            val viewModel: ProfileSharedViewModel = hiltViewModel(parentEntry)

            EditUsernameScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.EditBio.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.MyProfile.route)
            }
            val viewModel = hiltViewModel<ProfileSharedViewModel>(parentEntry)

            EditBioScreen(
                viewModel=viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(MainRoute.EditGender.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.MyProfile.route)
            }
            val viewModel = hiltViewModel<ProfileSharedViewModel>(parentEntry)

            EditGenderScreen(
                viewModel=viewModel,
                onBack= { navController.popBackStack() }
            )
        }

        composable("${MainRoute.UserSocial.route}/{initialPage}/{userId}/{username}",
            arguments = listOf(
                navArgument("initialPage") { type = NavType.IntType },
                navArgument("userId") { type = NavType.IntType },
                navArgument("username") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val initialPage = backStackEntry.arguments?.getInt("initialPage") ?: return@composable
            val username = backStackEntry.arguments?.getString("username") ?: return@composable

            val viewModel = hiltViewModel<UserSocialViewModel>(backStackEntry)

            UserSocialScreen(
                onBack = { navController.popBackStack() },
                viewModal = viewModel,
                initialPage = initialPage,
                username = username,
                onNavigateUserProfile = { navController.navigate(MainRoute.UserProfile.route) }
            )
        }
    }
}
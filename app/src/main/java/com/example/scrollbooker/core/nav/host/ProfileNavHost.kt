package com.example.scrollbooker.core.nav.host

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.scrollbooker.core.nav.navigators.myBusinessGraph
import com.example.scrollbooker.core.nav.navigators.settingsGraph
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.nav.transitions.slideInFromLeft
import com.example.scrollbooker.core.nav.transitions.slideInFromRight
import com.example.scrollbooker.core.nav.transitions.slideOutToLeft
import com.example.scrollbooker.core.nav.transitions.slideOutToRight
import com.example.scrollbooker.feature.profile.presentation.MyProfileScreen
import com.example.scrollbooker.feature.profile.presentation.ProfileSharedViewModel
import com.example.scrollbooker.feature.profile.presentation.edit.EditBioScreen
import com.example.scrollbooker.feature.profile.presentation.edit.EditFullNameScreen
import com.example.scrollbooker.feature.profile.presentation.edit.EditGenderScreen
import com.example.scrollbooker.feature.profile.presentation.edit.EditProfileScreen
import com.example.scrollbooker.feature.profile.presentation.edit.EditUsernameScreen
import com.example.scrollbooker.feature.userSocial.presentation.UserSocialScreen
import com.example.scrollbooker.feature.userSocial.presentation.UserSocialViewModel

@Composable
fun ProfileNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.MyProfile.route
    ) {
        composable(MainRoute.MyProfile.route) { backStackEntry ->
            val viewModel = hiltViewModel<ProfileSharedViewModel>(backStackEntry)
            MyProfileScreen(
                viewModel = viewModel,
                onNavigate = { navController.navigate(it) }
            )
        }
        composable(MainRoute.EditProfile.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) { backStackEntry ->
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
        composable(
            MainRoute.EditFullName.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.MyProfile.route)
            }
            val viewModel: ProfileSharedViewModel = hiltViewModel(parentEntry)

            EditFullNameScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            MainRoute.EditUsername.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.MyProfile.route)
            }
            val viewModel: ProfileSharedViewModel = hiltViewModel(parentEntry)

            EditUsernameScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            MainRoute.EditBio.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.MyProfile.route)
            }
            val viewModel = hiltViewModel<ProfileSharedViewModel>(parentEntry)

            EditBioScreen(
                viewModel=viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            MainRoute.EditGender.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.MyProfile.route)
            }
            val viewModel = hiltViewModel<ProfileSharedViewModel>(parentEntry)

            EditGenderScreen(
                viewModel=viewModel,
                onBack= { navController.popBackStack() }
            )
        }

        composable(
            route = "${MainRoute.UserSocial.route}/{initialPage}/{userId}/{username}",
            arguments = listOf(
                navArgument("initialPage") { type = NavType.IntType },
                navArgument("userId") { type = NavType.IntType },
                navArgument("username") { type = NavType.StringType }
            ),
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) { backStackEntry ->
            val initialPage = backStackEntry.arguments?.getInt("initialPage") ?: return@composable
            val username = backStackEntry.arguments?.getString("username") ?: return@composable

            val viewModel = hiltViewModel<UserSocialViewModel>(backStackEntry)

            UserSocialScreen(
                onBack = { navController.popBackStack() },
                viewModal = viewModel,
                initialPage = initialPage,
                username = username
            )
        }

        myBusinessGraph(navController)
        settingsGraph(navController)
    }
}
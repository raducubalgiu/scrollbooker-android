package com.example.scrollbooker.core.nav.navigators

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.nav.transitions.slideInFromLeft
import com.example.scrollbooker.core.nav.transitions.slideInFromRight
import com.example.scrollbooker.core.nav.transitions.slideOutToLeft
import com.example.scrollbooker.core.nav.transitions.slideOutToRight
import com.example.scrollbooker.feature.profile.presentation.edit.EditBioScreen
import com.example.scrollbooker.feature.profile.presentation.edit.EditFullNameScreen
import com.example.scrollbooker.feature.profile.presentation.edit.EditGenderScreen
import com.example.scrollbooker.feature.profile.presentation.edit.EditProfileScreen
import com.example.scrollbooker.feature.profile.presentation.edit.EditUsernameScreen
import com.example.scrollbooker.feature.profile.presentation.ProfileScreen
import com.example.scrollbooker.feature.profile.presentation.ProfileSharedViewModel
//
//fun NavGraphBuilder.profileGraph(navController: NavController) {
//    navigation(
//        route = MainRoute.ProfileNavigator.route,
//        startDestination = MainRoute.Profile.route,
//    ) {
//        composable(MainRoute.Profile.route) { backStackEntry ->
//            val parentEntry = remember(backStackEntry) {
//                navController.getBackStackEntry(MainRoute.ProfileNavigator.route)
//            }
//            val sharedViewModel: ProfileSharedViewModel = hiltViewModel(parentEntry)
//
//            ProfileScreen(navController, sharedViewModel)
//        }
//        composable(MainRoute.EditProfile.route,
//            enterTransition = slideInFromRight(),
//            exitTransition = slideOutToLeft(),
//            popEnterTransition = slideInFromLeft(),
//            popExitTransition = slideOutToRight()
//        ) { backStackEntry ->
//            val parentEntry = remember(backStackEntry) {
//                navController.getBackStackEntry(MainRoute.ProfileNavigator.route)
//            }
//            val sharedViewModel: ProfileSharedViewModel = hiltViewModel(parentEntry)
//            EditProfileScreen(navController, sharedViewModel)
//        }
//
//        composable(
//            MainRoute.EditFullName.route,
//            enterTransition = slideInFromRight(),
//            exitTransition = slideOutToLeft(),
//            popEnterTransition = slideInFromLeft(),
//            popExitTransition = slideOutToRight()
//        ) { backStackEntry ->
//            val parentEntry = remember(backStackEntry) {
//                navController.getBackStackEntry(MainRoute.ProfileNavigator.route)
//            }
//            val sharedViewModel: ProfileSharedViewModel = hiltViewModel(parentEntry)
//
//            EditFullNameScreen(navController, sharedViewModel)
//        }
//
//        composable(
//            MainRoute.EditUsername.route,
//            enterTransition = slideInFromRight(),
//            exitTransition = slideOutToLeft(),
//            popEnterTransition = slideInFromLeft(),
//            popExitTransition = slideOutToRight()
//        ) { backStackEntry ->
//            val parentEntry = remember(backStackEntry) {
//                navController.getBackStackEntry(MainRoute.ProfileNavigator.route)
//            }
//            val sharedViewModel: ProfileSharedViewModel = hiltViewModel(parentEntry)
//
//            EditUsernameScreen(navController, sharedViewModel)
//        }
//
//        composable(
//            MainRoute.EditBio.route,
//            enterTransition = slideInFromRight(),
//            exitTransition = slideOutToLeft(),
//            popEnterTransition = slideInFromLeft(),
//            popExitTransition = slideOutToRight()
//        ) { backStackEntry ->
//            val parentEntry = remember(backStackEntry) {
//                navController.getBackStackEntry(MainRoute.ProfileNavigator.route)
//            }
//            val sharedViewModel: ProfileSharedViewModel = hiltViewModel(parentEntry)
//
//            EditBioScreen(navController, sharedViewModel)
//        }
//
//        composable(
//            MainRoute.EditGender.route,
//            enterTransition = slideInFromRight(),
//            exitTransition = slideOutToLeft(),
//            popEnterTransition = slideInFromLeft(),
//            popExitTransition = slideOutToRight()
//        ) { backStackEntry ->
//            val parentEntry = remember(backStackEntry) {
//                navController.getBackStackEntry(MainRoute.ProfileNavigator.route)
//            }
//            val sharedViewModel: ProfileSharedViewModel = hiltViewModel(parentEntry)
//
//            EditGenderScreen(navController, sharedViewModel)
//        }
//    }
//}
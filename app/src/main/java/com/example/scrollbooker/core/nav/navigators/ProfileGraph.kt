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
        startDestination = MainRoute.EditProfile.route,
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

    }
}
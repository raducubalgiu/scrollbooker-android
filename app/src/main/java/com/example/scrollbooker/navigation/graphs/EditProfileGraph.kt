package com.example.scrollbooker.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.profile.myProfile.MyProfileViewModel
import com.example.scrollbooker.ui.profile.myProfile.edit.EditBioScreen
import com.example.scrollbooker.ui.profile.myProfile.edit.EditFullNameScreen
import com.example.scrollbooker.ui.profile.myProfile.edit.EditGenderScreen
import com.example.scrollbooker.ui.profile.myProfile.edit.EditProfessionScreen
import com.example.scrollbooker.ui.profile.myProfile.edit.EditProfileScreen
import com.example.scrollbooker.ui.profile.myProfile.edit.EditUsernameScreen

fun NavGraphBuilder.editProfileGraph(
    navController: NavHostController,
    viewModel: MyProfileViewModel
) {
    navigation(
        route = MainRoute.EditProfileNavigator.route,
        startDestination = MainRoute.EditProfile.route
    ) {
        composable(
            route = MainRoute.EditProfile.route,
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition = { slideOutToRight() }
        ) {
            EditProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(
            route = MainRoute.EditFullName.route,
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition = { slideOutToRight() }
        ) {
            EditFullNameScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = MainRoute.EditUsername.route,
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition = { slideOutToRight() }
        ) {
            EditUsernameScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = MainRoute.EditProfession.route,
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition = { slideOutToRight() }
        ) {
            EditProfessionScreen(
                viewModel=viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = MainRoute.EditBio.route,
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition = { slideOutToRight() }
        ) {
            EditBioScreen(
                viewModel=viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = MainRoute.EditGender.route,
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition = { slideOutToRight() }
        ) {
            EditGenderScreen(
                viewModel=viewModel,
                onBack= { navController.popBackStack() }
            )
        }
    }
}
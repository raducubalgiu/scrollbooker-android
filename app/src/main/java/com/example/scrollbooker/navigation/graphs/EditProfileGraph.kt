package com.example.scrollbooker.navigation.graphs

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.scrollbooker.navigation.navigators.EditProfileNavigator
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.profile.MyProfileViewModel
import com.example.scrollbooker.ui.profile.edit.EditAvatarCropScreen
import com.example.scrollbooker.ui.profile.edit.EditBioScreen
import com.example.scrollbooker.ui.profile.edit.EditFullNameScreen
import com.example.scrollbooker.ui.profile.edit.EditGenderScreen
import com.example.scrollbooker.ui.profile.edit.EditProfileScreen
import com.example.scrollbooker.ui.profile.edit.EditPublicEmailScreen
import com.example.scrollbooker.ui.profile.edit.EditUsernameScreen
import com.example.scrollbooker.ui.profile.edit.EditWebsiteScreen

fun NavGraphBuilder.editProfileGraph(
    navController: NavHostController,
    viewModel: MyProfileViewModel
) {
    navigation(
        route = MainRoute.EditProfileNavigator.route,
        startDestination = MainRoute.EditProfile.route,
    ) {
        composable(route = MainRoute.EditProfile.route) {
            val editProfileNavigate = remember(navController) {
                EditProfileNavigator(
                    navController = navController
                )
            }

            EditProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToCropScreen = { editProfileNavigate.toEditAvatarCropScreen() },
                editProfileNavigate = editProfileNavigate,
            )
        }

        composable(route = MainRoute.EditAvatarCropScreen.route) {
            EditAvatarCropScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = MainRoute.EditFullName.route) {
            EditFullNameScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(route = MainRoute.EditUsername.route) {
            EditUsernameScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = MainRoute.EditBio.route) {
            EditBioScreen(
                viewModel=viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = MainRoute.EditGender.route) {
            EditGenderScreen(
                viewModel=viewModel,
                onBack= { navController.popBackStack() }
            )
        }

        composable(route = MainRoute.EditPublicEmail.route) {
            EditPublicEmailScreen(
                viewModel=viewModel,
                onBack= { navController.popBackStack() }
            )
        }

        composable(route = MainRoute.EditWebsite.route) {
            EditWebsiteScreen(
                viewModel=viewModel,
                onBack= { navController.popBackStack() }
            )
        }
    }
}
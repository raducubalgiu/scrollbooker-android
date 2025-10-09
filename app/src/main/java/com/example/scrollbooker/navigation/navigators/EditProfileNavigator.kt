package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.navigation.routes.MainRoute

class EditProfileNavigator (
    private val navController: NavHostController
) {
    fun toEditFullName() {
        navController.navigate(MainRoute.EditFullName.route)
    }
    fun toEditUsername() {
        navController.navigate(MainRoute.EditUsername.route)
    }
    fun toEditBio() {
        navController.navigate(MainRoute.EditBio.route)
    }
    fun toEditGender() {
        navController.navigate(MainRoute.EditGender.route)
    }
    fun toEditProfession() {
        navController.navigate(MainRoute.EditProfession.route)
    }
    fun toEditPublicEmail() {
        navController.navigate(MainRoute.EditPublicEmail.route)
    }
    fun toEditWebsite() {
        navController.navigate(MainRoute.EditWebsite.route)
    }
}
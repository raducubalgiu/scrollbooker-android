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
    fun toEditBirthdate() {
        navController.navigate(MainRoute.EditBirthDate.route)
    }
    fun toEditAvatarCropScreen() {
        navController.navigate(MainRoute.EditAvatarCropScreen.route)
    }

// V2 - Will be added in next iterations, for now it's hidden in the code

//    fun toEditPublicEmail() {
//        navController.navigate(MainRoute.EditPublicEmail.route)
//    }
//    fun toEditWebsite() {
//        navController.navigate(MainRoute.EditWebsite.route)
//    }
}
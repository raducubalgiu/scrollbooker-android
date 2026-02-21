package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.navigation.routes.MainRoute

class ProfileNavigator (
    private val navController: NavHostController
) {
    fun toCamera() {
        navController.navigate(MainRoute.Camera.route)
    }

    fun toUserProfile(ownerId: Int) {
        navController.navigate("${MainRoute.UserProfile.route}/${ownerId}")
    }

    fun toPostDetail() {
        navController.navigate(MainRoute.MyProfilePostDetail.route)
    }

    fun toUserPostDetail() {
        navController.navigate(MainRoute.UserProfilePostDetail.route)
    }

    fun toMyCalendar() {
        navController.navigate(MainRoute.MyCalendar.route)
    }

    fun toMyBusiness() {
        navController.navigate(MainRoute.MyBusinessNavigator.route)
    }

    fun toEditProfile(){
        navController.navigate(MainRoute.EditProfile.route)
    }

    fun toSettings () {
        navController.navigate(MainRoute.Settings.route)
    }
}
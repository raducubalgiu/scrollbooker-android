package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.navigation.routes.MainRoute

class ProfileNavigator (
    private val navController: NavHostController
) {
    fun toCamera() {
        navController.navigate(MainRoute.Camera.route) {
            launchSingleTop = true
        }
    }

    fun toUserProfile(ownerId: Int) {
        navController.navigate("${MainRoute.UserProfile.route}/${ownerId}") {
            launchSingleTop = true
        }
    }

    fun toPostDetail() {
        navController.navigate(MainRoute.MyProfilePostDetail.route) {
            launchSingleTop = true
        }
    }

    fun toUserPostDetail() {
        navController.navigate(MainRoute.UserProfilePostDetail.route) {
            launchSingleTop = true
        }
    }

    fun toMyCalendar() {
        navController.navigate(MainRoute.MyCalendar.route) {
            launchSingleTop = true
        }
    }

    fun toMyBusiness() {
        navController.navigate(MainRoute.MyBusinessNavigator.route) {
            launchSingleTop = true
        }
    }

    fun toEditProfile(){
        navController.navigate(MainRoute.EditProfile.route) {
            launchSingleTop = true
        }
    }

    fun toSettings () {
        navController.navigate(MainRoute.Settings.route) {
            launchSingleTop = true
        }
    }

    fun toSocial(socialParam: NavigateSocialParam) {
        val ( tabIndex, userId, username, isBusinessOrEmployee ) = socialParam

        navController.navigate(
            "${MainRoute.Social.route}/${tabIndex}/${userId}/${username}/${isBusinessOrEmployee}"
        )  {
            launchSingleTop = true
        }
    }
}
package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.navigation.routes.MainRoute

class ProfileNavigator (
    private val rootNavController: NavHostController,
    private val navController: NavHostController
) {
    fun toCamera() {
        rootNavController.navigate(MainRoute.Camera.route)
    }

    fun toSocial(socialParams: NavigateSocialParam) {
        val ( tabIndex, userId, username, isBusinessOrEmployee ) = socialParams

        rootNavController.navigate(
            "${MainRoute.Social.route}/${tabIndex}/${userId}/${username}/${isBusinessOrEmployee}"
        )
    }

    fun toUserProfile(ownerId: Int) {
        rootNavController.navigate("${MainRoute.UserProfile.route}/${ownerId}")
    }

    fun toPostDetail() {
        navController.navigate(MainRoute.MyProfilePostDetail.route)
    }

    fun toUserPostDetail() {
        rootNavController.navigate(MainRoute.MyProfilePostDetail.route)
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
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

    fun toBusinessOwner(ownerId: Int) {
        rootNavController.navigate("${MainRoute.UserProfile.route}/${ownerId}")
    }

    fun toPostDetail() {}

    fun toMyCalendar() {
        navController.navigate(MainRoute.MyCalendar.route)
    }

    fun toCalendar (calendarParams: NavigateCalendarParam) {
        val (userId, slotDuration, productId, productName) = calendarParams

        rootNavController.navigate(
            "${MainRoute.Calendar.route}/${userId}/${slotDuration}/${productId}/${productName}"
        )
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
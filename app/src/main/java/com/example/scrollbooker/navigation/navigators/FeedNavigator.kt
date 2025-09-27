package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.navigation.routes.MainRoute

class FeedNavigator (
    private val rootNavController: NavHostController,
    private val navController: NavHostController
) {
    fun toFeedSearch() {
        navController.navigate(MainRoute.FeedSearch.route)
    }

    fun toUserProducts(userId: Int) {
        rootNavController.navigate("${MainRoute.UserProducts.route}/${userId}")
    }

    fun toUserProfile(userId: Int) {
        rootNavController.navigate("${MainRoute.UserProfile.route}/${userId}")
    }

    fun toCamera() {
        rootNavController.navigate(MainRoute.Camera.route)
    }

    fun toCalendar (calendarParams: NavigateCalendarParam) {
        val (userId, slotDuration, productId, productName) = calendarParams
        rootNavController.navigate(
            "${MainRoute.Calendar.route}/${userId}/${slotDuration}/${productId}/${productName}"
        )
    }
}
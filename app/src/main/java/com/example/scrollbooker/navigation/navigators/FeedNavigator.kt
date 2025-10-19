package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.navigation.routes.MainRoute

class FeedNavigator (
    private val mainNavController: NavHostController,
    private val navController: NavHostController
) {
    fun toFeedSearch() {
        navController.navigate(MainRoute.FeedSearch.route)
    }

    fun toUserProducts(userId: Int) {
        mainNavController.navigate("${MainRoute.UserProducts.route}/${userId}")
    }

    fun toUserProfile(userId: Int) {
        mainNavController.navigate("${MainRoute.UserProfile.route}/${userId}")
    }

    fun toCalendar (calendarParams: NavigateCalendarParam) {
        val (userId, slotDuration, productId, productName) = calendarParams
        mainNavController.navigate(
            "${MainRoute.Calendar.route}/${userId}/${slotDuration}/${productId}/${productName}"
        )
    }
}
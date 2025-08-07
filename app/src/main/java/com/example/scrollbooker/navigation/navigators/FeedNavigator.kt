package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.navigation.routes.MainRoute

class FeedNavigator (private val navController: NavHostController) {
    fun toFeedSearch() {
        navController.navigate(MainRoute.FeedSearch.route)
    }

    fun toUserProfile(userId: Int) {
        navController.navigate("${MainRoute.UserProfile.route}/${userId}")
    }

    fun toCalendar (calendarParams: NavigateCalendarParam) {
        val (userId, slotDuration, productId, productName) = calendarParams
        navController.navigate(
            "${MainRoute.Calendar.route}/${userId}/${slotDuration}/${productId}/${productName}"
        )
    }
}
package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.navigation.routes.MainRoute

class FeedNavigator (
    private val navController: NavHostController
) {
    fun toFeedSearch() {
        navController.navigate(MainRoute.FeedSearch.route) {
            launchSingleTop = true
        }
    }

    fun toUserProfile(userId: Int, username: String) {
        navController.navigateToUserProfile(userId, username)
    }

    fun toBooking(params: NavigateBookingParam) {
        navController.navigateToBookingFromProfile(
            userId = params.userId,
            businessId = params.businessId,
            businessOwnerId = params.businessOwnerId,
            source = params.source,
            selectedProductId = params.selectedProductId
        )
    }
}
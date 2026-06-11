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

    fun toBooking(
        userId: Int,
        businessId: Int,
        businessOwnerId: Int,
        source: String,
        selectedProductId: Int?
    ) {
        navController.navigateToBookingFromProfile(
            businessId = businessId,
            userId = userId,
            businessOwnerId = businessOwnerId,
            source = source,
            selectedProductId = selectedProductId
        )
    }
}
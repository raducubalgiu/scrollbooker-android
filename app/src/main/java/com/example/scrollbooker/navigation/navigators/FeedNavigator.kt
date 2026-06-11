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
        navController.navigate("${MainRoute.UserProfile.route}/${userId}/${username}") {
            launchSingleTop = true
        }
    }

    fun toBooking(
        userId: Int,
        businessId: Int,
        businessOwnerId: Int,
        source: String,
        selectedProductId: Int?
    ) {
        var route = "bookingNavigator/$businessId/$userId/$businessOwnerId/$source"

        if (selectedProductId != null) {
            route += "?selectedProductId=$selectedProductId"
        }

        navController.navigate(route) {
            launchSingleTop = true
        }
    }
}
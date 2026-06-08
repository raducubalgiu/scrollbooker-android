package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.navigation.routes.MainRoute

class SearchNavigator (
    private val navController: NavHostController
) {
    fun toBusinessProfile(businessOwnerUsername: String) {
        navController.navigate((MainRoute.BusinessProfile.createRoute(businessOwnerUsername))) {
            launchSingleTop = true
        }
    }

    fun toUserProfile(userId: Int, username: String) {
        navController.navigate("${MainRoute.UserProfile.route}/${userId}/${username}") {
            launchSingleTop = true
        }
    }

    fun toBooking(businessId: Int, employeeId: Int?, selectedProductId: Int?) {
        var route = "bookingNavigator/$businessId"

        val queryParams = mutableListOf<String>()

        if (employeeId != null) {
            queryParams.add("employeeId=$employeeId")
        }

        if (selectedProductId != null) {
            queryParams.add("selectedProductId=$selectedProductId")
        }

        if (queryParams.isNotEmpty()) {
            route += "?" + queryParams.joinToString("&")
        }

        navController.navigate(route) {
            launchSingleTop = true
        }
    }
}
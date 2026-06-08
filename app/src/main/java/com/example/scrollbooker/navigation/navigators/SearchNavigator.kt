package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.entity.booking.products.domain.model.Product
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

    fun toBooking(product: Product) {
        val uniqueUserIds = product.variants
            .flatMap { it.offerings }
            .map { it.user.id }
            .distinct()

        val employeeId = when {
            uniqueUserIds.size == 1 -> {
                val singleUserId = uniqueUserIds.first()
                if (singleUserId == product.businessOwnerId) {
                    null
                } else {
                    singleUserId
                }
            }
            else -> null
        }

        var route = "bookingNavigator/${product.businessId}"

        val queryParams = mutableListOf<String>()

        if (employeeId != null) {
            queryParams.add("employeeId=$employeeId")
        }

        queryParams.add("selectedProductId=${product.id}")

        if (queryParams.isNotEmpty()) {
            route += "?" + queryParams.joinToString("&")
        }

        navController.navigate(route) {
            launchSingleTop = true
        }
    }
}
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

    fun toBookingFromBusinessProfile(
        businessId: Int,
        userId: Int,
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

    fun toBookingFromProduct(product: Product, source: String) {
        val uniqueUserIds = product.variants
            .flatMap { it.offerings }
            .map { it.user.id }
            .distinct()

        val targetUserId = when {
            uniqueUserIds.size == 1 -> uniqueUserIds.first()
            else -> product.businessOwnerId
        }

        var route = "bookingNavigator/${product.businessId}/$targetUserId/${product.businessOwnerId}/$source"
        route += "?selectedProductId=${product.id}"

        navController.navigate(route) {
            launchSingleTop = true
        }
    }
}
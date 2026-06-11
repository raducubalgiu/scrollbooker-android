package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.navigation.routes.MainRoute

fun NavHostController.navigateToUserProfile(userId: Int, username: String) {
    this.navigate("${MainRoute.UserProfile.route}/${userId}/${username}") {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToBookingFromProfile(
    businessId: Int,
    userId: Int,
    businessOwnerId: Int,
    source: String,
    selectedProductId: Int?
) {
    val route = MainRoute.BookingNavigator.createRouteFromProfile(
        businessId = businessId,
        userId = userId,
        businessOwnerId = businessOwnerId,
        source = source,
        selectedProductId = selectedProductId
    )

    this.navigate(route) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToBookingFromProduct(
    product: Product,
    source: String
) {
    val route = MainRoute.BookingNavigator.createRouteFromProduct(
        product = product,
        source = source
    )

    this.navigate(route) {
        launchSingleTop = true
    }
}


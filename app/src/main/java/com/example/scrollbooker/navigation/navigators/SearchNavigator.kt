package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.core.enums.BookingSourceEnum
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.navigation.routes.MainRoute

class SearchNavigator (
    private val navController: NavHostController
) {
    fun toBusinessProfile(ownerUsername: String, ownerProfession: String) {
        navController.navigate((MainRoute.BusinessProfile.createRoute(ownerUsername, ownerProfession))) {
            launchSingleTop = true
        }
    }

    fun toUserProfile(param: UserProfileParam) {
        navController.navigateToUserProfile(param)
    }

    fun toBookingFromProduct(product: Product, source: BookingSourceEnum) {
        navController.navigateToBookingFromProduct(product, source)
    }

    fun toBookingFromBusinessProfile(
        businessId: Int,
        userId: Int,
        businessOwnerId: Int,
        source: BookingSourceEnum,
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
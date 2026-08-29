package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.core.enums.BookingSourceEnum
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.navigation.routes.MainRoute

class ReviewsNavigator(
    private val navController: NavHostController
) {
    fun back() {
        navController.popBackStack()
    }

    fun toReviewDetail(param: ReviewsDetailParam) {
        val route = MainRoute.ReviewsDetail.createRoute(param)

        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    fun toUserProfile(param: UserProfileParam) {
        navController.navigateToUserProfile(param)
    }

    fun toBookingFromProduct(product: Product, source: BookingSourceEnum) {
        navController.navigateToBookingFromProduct(product, source)
    }

    fun toEditPost(postId: Int) {
        navController.navigate(MainRoute.EditPostNavigator.createRoute(postId)) {
            launchSingleTop = true
        }
    }
}

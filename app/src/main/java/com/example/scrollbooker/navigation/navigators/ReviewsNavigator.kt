package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.core.enums.BookingSourceEnum
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
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

    fun toBookingFromAppointment(appointment: Appointment) {
        navController.navigateToBookingFromAppointment(appointment, BookingSourceEnum.VIDEO_REVIEWS)
    }

    fun toBookingFromProfile(businessId: Int, userId: Int, businessOwnerId: Int) {
        navController.navigateToBookingFromProfile(
            businessId = businessId,
            userId = userId,
            businessOwnerId = businessOwnerId,
            source = BookingSourceEnum.VIDEO_REVIEWS,
            selectedProductId = null
        )
    }

    fun toEditPost(postId: Int) {
        navController.navigate(MainRoute.PostUtilityNavigator.createRoute(postId)) {
            launchSingleTop = true
        }
    }

    fun toPostStatistics(postId: Int) {
        navController.navigate(MainRoute.PostUtilityNavigator.createRoute(postId)) {
            launchSingleTop = true
        }
        navController.navigate(MainRoute.PostStatistics.route) {
            popUpTo(MainRoute.EditPost.route) { inclusive = true }
        }
    }
}

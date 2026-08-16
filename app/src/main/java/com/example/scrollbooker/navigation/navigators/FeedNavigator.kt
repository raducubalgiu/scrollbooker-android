package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.core.enums.BookingSourceEnum
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.routes.MainRoute

class FeedNavigator (
    private val navController: NavHostController
) {
    fun back() {
        navController.popBackStack()
    }

    fun toFeedSearch() {
        navController.navigate(MainRoute.FeedSearch.route) {
            launchSingleTop = true
        }
    }

    fun toUserProfile(param: UserProfileParam) {
        navController.navigateToUserProfile(param)
    }

    fun toBookingFromPost(post: Post, source: BookingSourceEnum) {
        navController.navigateToBookingFromPost(
            post = post,
            source = source
        )
    }

    fun toBookingFromProduct(product: Product, source: BookingSourceEnum) {
        navController.navigateToBookingFromProduct(
            product = product,
            source = source
        )
    }

    fun toEditPost(postId: Int) {
        navController.navigate("${MainRoute.EditPost.route}/$postId")
    }
}
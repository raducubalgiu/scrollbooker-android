package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.core.enums.BookingSourceEnum
import com.example.scrollbooker.entity.social.post.domain.model.Post
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

    fun toBookingFromPost(post: Post, source: BookingSourceEnum) {
        navController.navigateToBookingFromPost(
            post = post,
            source = source
        )
    }
}
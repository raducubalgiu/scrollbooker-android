package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.navigation.routes.MainRoute

class FeedNavigator (
    private val mainNavController: NavHostController,
    private val navController: NavHostController
) {
    fun toFeedSearch() {
        navController.navigate(MainRoute.FeedSearch.route)
    }

    fun toUserProfile(userId: Int) {
        mainNavController.navigate("${MainRoute.UserProfile.route}/${userId}")
    }
}
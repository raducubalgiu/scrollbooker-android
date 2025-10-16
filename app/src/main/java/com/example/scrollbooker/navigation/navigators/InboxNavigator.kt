package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.navigation.routes.MainRoute

class InboxNavigator (
    private val mainNavController: NavHostController,
    private val navController: NavHostController
) {
    fun toEmploymentRespond(employmentId: Int) {
        navController.navigate("${MainRoute.EmploymentRespond.route}/${employmentId}")
    }

    fun toUserProfile(userId: Int) {
        mainNavController.navigate("${MainRoute.UserProfile.route}/${userId}")
    }
}
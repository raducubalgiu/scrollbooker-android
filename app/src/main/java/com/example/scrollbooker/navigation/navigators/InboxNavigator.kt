package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.navigation.routes.MainRoute

class InboxNavigator (
    private val rootNavController: NavHostController,
    private val navController: NavHostController
) {
    fun toEmploymentRespond(employmentId: Int) {
        navController.navigate("${MainRoute.EmploymentRequestRespond.route}/${employmentId}")
    }

    fun toUserProfile(userId: Int) {
        rootNavController.navigate("${MainRoute.UserProfile.route}/${userId}")
    }
}
package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.navigation.routes.MainRoute

class SettingsNavigator (
    private val navController: NavHostController
) {
    fun toDisplay() {
        navController.navigate(MainRoute.Display.route)
    }
    fun toReportProblem() {
        navController.navigate(MainRoute.ReportProblem.route)
    }

    // V2 - Will be added in next iterations, for now it's hidden in the code

//    fun toAccount() {
//        navController.navigate(MainRoute.Account.route)
//    }
//    fun toPrivacy() {
//        navController.navigate(MainRoute.Privacy.route)
//    }
//    fun toSecurity() {
//        navController.navigate(MainRoute.Security.route)
//    }
//    fun toNotifications() {
//        navController.navigate(MainRoute.NotificationSettings.route)
//    }
//    fun toSupport() {
//        navController.navigate(MainRoute.Support.route)
//    }
//    fun toTermsAndConditions() {
//        navController.navigate(MainRoute.TermsAndConditions.route)
//    }
}
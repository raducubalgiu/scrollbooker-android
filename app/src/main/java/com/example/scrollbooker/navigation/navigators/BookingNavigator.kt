package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.navigation.routes.MainRoute

class BookingNavigator (
    private val navController: NavHostController
) {
    fun back() {
        navController.popBackStack()
    }

    fun toSpecialists() {
        navController.navigate(MainRoute.BookingSpecialists.route)
    }

    fun toDateTime() {
        navController.navigate(MainRoute.BookingDateTime.route)
    }

    fun toConfirmation() {
        navController.navigate(MainRoute.BookingConfirmation.route)
    }
}
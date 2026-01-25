package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.navigation.routes.MainRoute

class MyBusinessNavigator (
    private val navController: NavHostController
) {
    fun toMyBusinessLocation() {
        navController.navigate(MainRoute.MyBusinessLocation.route)
    }
    fun toMySchedules() {
        navController.navigate(MainRoute.MySchedules.route)
    }
    fun toMyClasses() {
        navController.navigate(MainRoute.MyClasses.route)
    }
    fun toMyProducts() {
        navController.navigate(MainRoute.MyProducts.route)
    }
    fun toMyServices() {
        navController.navigate(MainRoute.MyServices.route)
    }
    fun toMyCalendar() {
        navController.navigate(MainRoute.MyCalendar.route)
    }
    fun toMyCurrencies() {
        navController.navigate(MainRoute.MyCurrencies.route)
    }
    fun toMyEmployees() {
        navController.navigate(MainRoute.MyEmployees.route)
    }
    fun toMyEmploymentRequests() {
        navController.navigate(MainRoute.EmploymentsRequests.route)
    }
}
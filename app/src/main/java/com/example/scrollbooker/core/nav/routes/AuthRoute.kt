package com.example.scrollbooker.core.nav.routes

sealed class AuthRoute(val route: String) {
    object Login: AuthRoute(route = "login")
    object Register: AuthRoute(route = "register")
    object Username: AuthRoute(route = "username")
    object BirthDate: AuthRoute(route = "birthdate")
    object BusinessLocation: AuthRoute(route = "businessLocation")
    object BusinessServices: AuthRoute(route = "businessServices")
    object BusinessSchedules: AuthRoute(route = "businessSchedules")
}
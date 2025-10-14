package com.example.scrollbooker.navigation.routes

sealed class AuthRoute(val route: String) {
    object Login: AuthRoute(route = "login")

    object RegisterClient: AuthRoute(route = "registerClient")
    object RegisterBusiness: AuthRoute(route = "registerBusiness")
}
package com.example.scrollbooker.core.nav.routes

sealed class MainRoute(val route: String) {
    object Feed: MainRoute(route = "feed")
    object Inbox: MainRoute(route = "inbox")
    object Search: MainRoute(route = "search")
    object Appointments: MainRoute(route = "appointments")

    object ProfileNavigator: MainRoute(route = "profileNavigator")
    object Profile: MainRoute(route = "profile")
    object Settings: MainRoute(route = "settings")
}
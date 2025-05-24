package com.example.scrollbooker.core.nav.routes

sealed class MainRoute(val route: String) {
    object Feed: MainRoute(route = "feed")
    object Inbox: MainRoute(route = "inbox")
    object Search: MainRoute(route = "search")
    object Appointments: MainRoute(route = "appointments")
    object Profile: MainRoute(route = "profile")
}
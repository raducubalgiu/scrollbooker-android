package com.example.scrollbooker.core.nav.routes

sealed class MainRoute(val route: String) {
    object Feed: MainRoute(route = "feed")
    object Inbox: MainRoute(route = "inbox")
    object Search: MainRoute(route = "search")
    object Appointments: MainRoute(route = "appointments")

    object ProfileNavigator: MainRoute(route = "profileNavigator")
    object Profile: MainRoute(route = "profile")

    object SettingsNavigator: MainRoute(route = "settingsNavigator")
    object Account: MainRoute(route = "Account")
    object Privacy: MainRoute(route = "Privacy")
    object Security: MainRoute(route = "Security")
    object Settings: MainRoute(route = "settings")
    object NotificationSettings: MainRoute(route = "notificationSettings")
    object ReportProblem: MainRoute(route = "reportProblem")
}
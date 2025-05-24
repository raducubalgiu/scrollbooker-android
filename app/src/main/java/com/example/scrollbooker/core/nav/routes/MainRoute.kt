package com.example.scrollbooker.core.nav.routes

sealed class MainRoute(val route: String) {
    object Feed: MainRoute(route = "feed")
    object Inbox: MainRoute(route = "inbox")
    object Search: MainRoute(route = "search")
    object Appointments: MainRoute(route = "appointments")

    object ProfileNavigator: MainRoute(route = "profileNavigator")
    object Profile: MainRoute(route = "profile")

    object SettingsNavigator: MainRoute(route = "settingsNavigator")
    object Settings: MainRoute(route = "settings")
    object Account: MainRoute(route = "account")
    object Privacy: MainRoute(route = "privacy")
    object Security: MainRoute(route = "security")
    object NotificationSettings: MainRoute(route = "notificationSettings")
    object Display: MainRoute(route = "display")
    object ReportProblem: MainRoute(route = "reportProblem")
    object Support: MainRoute(route = "support")
    object TermsAndConditions: MainRoute(route = "termsAndConditions")
}
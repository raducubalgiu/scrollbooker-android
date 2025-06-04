package com.example.scrollbooker.core.nav.routes

sealed class MainRoute(val route: String) {
    object Feed: MainRoute(route = "feed")
    object Inbox: MainRoute(route = "inbox")
    object Search: MainRoute(route = "search")

    object AppointmentsNavigator: MainRoute(route = "appointmentsNavigator")
    object Appointments: MainRoute(route = "appointments")
    object AppointmentDetails: MainRoute(route = "appointmentDetails")

    object Profile: MainRoute(route = "profile")
    object EditProfile: MainRoute(route = "editProfile")
    object EditFullName: MainRoute(route = "editFullName")
    object EditUsername: MainRoute(route = "editUsername")
    object EditBio: MainRoute(route = "editBio")
    object EditGender: MainRoute(route = "editGender")

    object Calendar: MainRoute(route = "calendar")

    object MyBusinessNavigator: MainRoute(route = "myBusinessNavigator")
    object MyBusiness: MainRoute(route = "myBusiness")
    object Services: MainRoute(route = "services")
    object Products: MainRoute(route = "products")
    object AddProduct: MainRoute(route = "addProduct")
    object Schedules: MainRoute(route = "schedules")

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
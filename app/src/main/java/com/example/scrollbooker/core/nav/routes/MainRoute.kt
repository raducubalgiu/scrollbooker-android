package com.example.scrollbooker.core.nav.routes

sealed class MainRoute(val route: String) {
    object Feed: MainRoute(route = "feed")
    object FeedSearch: MainRoute(route = "feedSearch")

    object Inbox: MainRoute(route = "inbox")
    object EmploymentRequestRespondNavigator: MainRoute(route = "employmentRequestRespondNavigator")
    object EmploymentRequestRespond: MainRoute(route = "employmentRequestRespond")
    object EmploymentRequestRespondConsent: MainRoute(route = "employmentRequestRespondConsent")

    object Search: MainRoute(route = "search")
    object BusinessProfile: MainRoute(route = "businessProfile")

    object AppointmentsNavigator: MainRoute(route = "appointmentsNavigator")
    object Appointments: MainRoute(route = "appointments")
    object AppointmentDetails: MainRoute(route = "appointmentDetails")

    object MyProfile: MainRoute(route = "profile")
    object ProfilePostDetail: MainRoute(route = "profilePostDetail")
    object UserProfile: MainRoute(route = "userProfile")
    object EditProfile: MainRoute(route = "editProfile")
    object EditFullName: MainRoute(route = "editFullName")
    object EditUsername: MainRoute(route = "editUsername")
    object EditBio: MainRoute(route = "editBio")
    object EditGender: MainRoute(route = "editGender")
    object EditProfession: MainRoute(route = "editProfession")
    object UserSocial: MainRoute(route = "userSocial")

    object CalendarNavigator: MainRoute(route = "calendarNavigator")
    object Calendar: MainRoute(route = "calendar")
    object AppointmentConfirmation: MainRoute(route = "appointmentConfirmation")


    object Camera: MainRoute(route = "camera")

    object MyBusinessNavigator: MainRoute(route = "myBusinessNavigator")
    object Employees: MainRoute(route = "employees")
    object EmployeesDismissal: MainRoute(route = "employeesDismissal")
    object EmploymentsRequests: MainRoute(route = "employmentRequests")
    object EmploymentRequestsFlow: MainRoute(route = "employmentRequestsFlow")
    object EmploymentSelectEmployee: MainRoute(route = "employmentSelectEmployee")
    object EmploymentAssignJob: MainRoute(route = "employmentAssignJob")
    object EmploymentAcceptTerms: MainRoute(route = "employmentAcceptTerms")
    object MyCalendar: MainRoute(route = "myCalendar")
    object MyBusiness: MainRoute(route = "myBusiness")
    object MyServices: MainRoute(route = "myServices")
    object MyCurrencies: MainRoute(route = "myCurrencies")

    object MyProductsNavigator: MainRoute(route = "myProductsNavigator")
    object MyProducts: MainRoute(route = "myProducts")
    object AddProduct: MainRoute(route = "addProduct")
    object EditProduct: MainRoute(route = "editProduct")

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
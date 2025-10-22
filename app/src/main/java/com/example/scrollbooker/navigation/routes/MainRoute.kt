package com.example.scrollbooker.navigation.routes

sealed class MainRoute(val route: String) {
    object Shell: MainRoute(route = "shell")
    object Tabs: MainRoute(route = "tabs")

    object Feed: MainRoute(route = "feed")

    object FeedSearchNavigator: MainRoute(route = "feedSearchNavigator")
    object FeedSearch: MainRoute(route = "feedSearch")
    object FeedSearchResults: MainRoute(route = "feedSearchResults")

    object InboxNavigator: MainRoute(route = "inboxNavigator")
    object Inbox: MainRoute(route = "inbox")
    object EmploymentRespond: MainRoute(route = "employmentRespond")
    object EmploymentRespondConsent: MainRoute(route = "employmentRespondConsent")

    object Search: MainRoute(route = "search")
    object BusinessProfile: MainRoute(route = "businessProfile")

    object AppointmentsNavigator: MainRoute(route = "appointmentsNavigator")
    object Appointments: MainRoute(route = "appointments")
    object AppointmentDetails: MainRoute(route = "appointmentDetails")
    object AppointmentCancel: MainRoute(route = "appointmentCancel")

    object MyProfileNavigator: MainRoute(route = "myProfileNavigator")
    object MyProfile: MainRoute(route = "myProfile")
    object ProfilePostDetail: MainRoute(route = "profilePostDetail")
    object UserProfile: MainRoute(route = "userProfile")
    object UserProducts: MainRoute(route = "userProducts")

    object EditProfileNavigator: MainRoute(route = "editProfileNavigator")
    object EditProfile: MainRoute(route = "editProfile")
    object EditFullName: MainRoute(route = "editFullName")
    object EditUsername: MainRoute(route = "editUsername")
    object EditBio: MainRoute(route = "editBio")
    object EditGender: MainRoute(route = "editGender")
    object EditProfession: MainRoute(route = "editProfession")
    object EditWebsite: MainRoute(route = "website")
    object EditPublicEmail: MainRoute(route = "publicEmail")

    object Social: MainRoute(route = "social")
    object ReviewDetail: MainRoute(route = "reviewDetail")

    object CalendarNavigator: MainRoute(route = "calendarNavigator")
    object Calendar: MainRoute(route = "calendar")
    object AppointmentConfirmation: MainRoute(route = "appointmentConfirmation")

    object CameraNavigator: MainRoute(route = "cameraNavigator")
    object Camera: MainRoute(route = "camera")
    object CameraGallery: MainRoute(route = "cameraGallery")
    object CameraPreview: MainRoute(route = "cameraPreview")
    object CreatePost: MainRoute(route = "createPost")

    object MyBusinessNavigator: MainRoute(route = "myBusinessNavigator")
    object MyBusiness: MainRoute(route = "myBusiness")

    object MySchedules: MainRoute(route = "mySchedules")
    object MyServices: MainRoute(route = "myServices")
    object MyCalendar: MainRoute(route = "myCalendar")
    object MyCurrencies: MainRoute(route = "myCurrencies")

    object MyProductsNavigator: MainRoute(route = "myProductsNavigator")
    object MyProducts: MainRoute(route = "myProducts")
    object AddProduct: MainRoute(route = "addProduct")
    object EditProduct: MainRoute(route = "editProduct")

    object MyEmployees: MainRoute(route = "myEmployees")
    object EmployeesDismissal: MainRoute(route = "employeesDismissal")

    object EmploymentRequestsNavigator: MainRoute(route = "employmentRequestsNavigator")
    object EmploymentsRequests: MainRoute(route = "employmentRequests")
    object EmploymentSelectEmployee: MainRoute(route = "employmentSelectEmployee")
    object EmploymentAssignJob: MainRoute(route = "employmentAssignJob")
    object EmploymentAcceptTerms: MainRoute(route = "employmentAcceptTerms")

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

    object GlobalRouteNavigator: MainRoute(route = "globalRouteNavigator")
}
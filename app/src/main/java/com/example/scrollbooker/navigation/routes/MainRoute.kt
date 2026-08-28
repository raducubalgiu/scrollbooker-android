package com.example.scrollbooker.navigation.routes

import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.navigation.navigators.CameraParams
import com.example.scrollbooker.navigation.navigators.ProfilePostDetailParam
import com.example.scrollbooker.navigation.navigators.SocialParam
import com.example.scrollbooker.navigation.navigators.UserProfileParam

sealed class MainRoute(val route: String) {
    object Shell: MainRoute(route = "shell")
    object Tabs: MainRoute(route = "tabs")

    object Feed: MainRoute(route = "feed")
    object FeedSearch: MainRoute(route = "feedSearch")

    object InboxNavigator: MainRoute(route = "inboxNavigator")
    object Inbox: MainRoute(route = "inbox")
    object EmploymentRespond: MainRoute(route = "employmentRespond")
    object EmploymentRespondConsent: MainRoute(route = "employmentRespondConsent")

    object Search: MainRoute(route = "search")
    object BusinessProfile : MainRoute(route = "businessProfile?ownerUsername={ownerUsername}&profession={profession}") {
        fun createRoute(ownerUsername: String, profession: String): String {
            return "businessProfile?ownerUsername=$ownerUsername&profession=$profession"
        }
    }

    object BookingServices: MainRoute(route = "bookingServices")
    object BookingSpecialists: MainRoute(route = "bookingSpecialists")
    object BookingDateTime: MainRoute(route = "bookingDateTime")
    object BookingConfirmation: MainRoute(route = "bookingConfirmation")

    object Appointments: MainRoute(route = "appointments")
    object AppointmentDetails: MainRoute(route = "appointmentDetails")

    object ProfileNavigator: MainRoute(route = "profileNavigator")
    object MyProfileNavigator: MainRoute(route = "myProfileNavigator")
    object MyProfile: MainRoute(route = "myProfile")

    object MyProfilePostDetail: MainRoute(route = "myProfilePostDetail/{postTab}/{postIndex}") {
        fun createRoute(param: ProfilePostDetailParam): String {
            return "myProfilePostDetail/${param.postTab}/${param.postIndex}"
        }
    }

    object UserProfile: MainRoute(route = "userProfile/{userId}/{username}/{profession}") {
        fun createRoute(param: UserProfileParam): String {
            return "userProfile/${param.userId}/${param.username}/${param.profession}"
        }
    }
    object UserProfilePostDetail: MainRoute(route = "userProfilePostDetail/{postTab}/{postIndex}/{userId}") {
        fun createRoute(param: ProfilePostDetailParam): String {
            return "userProfilePostDetail/${param.postTab}/${param.postIndex}/${param.userId}"
        }
    }

    object EditProfileNavigator: MainRoute(route = "editProfileNavigator")
    object EditProfile: MainRoute(route = "editProfile")
    object EditAvatarCropScreen: MainRoute(route = "editAvatarCropScreen")
    object EditFullName: MainRoute(route = "editFullName")
    object EditUsername: MainRoute(route = "editUsername")
    object EditBio: MainRoute(route = "editBio")
    object EditGender: MainRoute(route = "editGender")
    object EditBirthDate: MainRoute(route = "editBirthDate")
    object EditWebsite: MainRoute(route = "website")
    object EditPublicEmail: MainRoute(route = "publicEmail")

    object Social: MainRoute(route = "social/{tabIndex}/{userId}/{username}/{businessId}/{employeeId}/{isBusinessOrEmployee}") {
        fun createRoute(param: SocialParam): String {
            return "social/${param.tabIndex}/${param.userId}/${param.username}/${param.businessId}/${param.employeeId}/${param.isBusinessOrEmployee}"
        }
    }

    object CameraNavigator: MainRoute(route = "cameraNavigator?appointmentId={appointmentId}&businessOrEmployeeId={businessOrEmployeeId}") {
        fun createRoute(param: CameraParams): String {
            return "cameraNavigator?appointmentId=${param.appointmentId}&businessOrEmployeeId=${param.businessOrEmployeeId}"
        }
    }
    object Camera: MainRoute(route = "camera")
    object CameraGallery: MainRoute(route = "cameraGallery")
    object CameraPreview: MainRoute(route = "cameraPreview")
    object CreatePost: MainRoute(route = "createPost")
    object CreatePostPreview: MainRoute(route = "createPostPreview")
    object CreatePostCover: MainRoute(route = "createPostCover")

    object EditPost: MainRoute(route = "editPost")

    object MyBusinessNavigator: MainRoute(route = "myBusinessNavigator")
    object MyBusiness: MainRoute(route = "myBusiness")

    object MyDashboard: MainRoute(route = "myDashboard")
    object UnapprovedBusinesses: MainRoute(route = "unapprovedBusinesses")
    object MyBusinessDetails: MainRoute(route = "myBusinessDetails")

    object MySchedules: MainRoute(route = "mySchedules")
    object MyServices: MainRoute(route = "myServices")
    object MyCalendar: MainRoute(route = "myCalendar")

    object MyProductsNavigator: MainRoute(route = "myProductsNavigator")
    object MyProducts: MainRoute(route = "myProducts")
    object AddProduct: MainRoute(route = "addProduct")
    object EditProduct: MainRoute(route = "editProduct/{productId}") {
        fun createRoute(productId: Int): String {
            return "editProduct/$productId"
        }
    }

    object MyEmployeesNavigator: MainRoute(route = "myEmployeesNavigator/{tabIndex}")
    object MyEmployees: MainRoute(route = "myEmployees")
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

    object BookingNavigator : MainRoute(
        route = "bookingNavigator/{businessId}/{userId}/{businessOwnerId}/{source}?selectedProductId={selectedProductId}&postId={postId}"
    ) {
        fun createRoute(
            businessId: Int,
            userId: Int,
            businessOwnerId: Int,
            source: String,
            selectedProductId: Int? = null,
            postId: Int? = null
        ): String {
            return "bookingNavigator/${businessId}/${userId}/${businessOwnerId}/${source}?selectedProductId=${selectedProductId}&postId=${postId}"
        }

        fun createRouteFromProfile(
            businessId: Int,
            userId: Int,
            businessOwnerId: Int,
            source: String,
            selectedProductId: Int? = null
        ): String {
            val base = "bookingNavigator/$businessId/$userId/$businessOwnerId/$source"
            return if (selectedProductId != null && selectedProductId != -1) {
                "$base?selectedProductId=$selectedProductId"
            } else {
                base
            }
        }

        fun createRouteFromProduct(
            product: Product,
            source: String
        ): String {
            val uniqueUserIds = product.variants
                .flatMap { it.offerings }
                .map { it.user.id }
                .distinct()

            val targetUserId = when {
                uniqueUserIds.size == 1 -> uniqueUserIds.first()
                else -> product.businessOwnerId
            }

            return "bookingNavigator/${product.businessId}/$targetUserId/${product.businessOwnerId}/$source?selectedProductId=${product.id}"
        }
    }
}
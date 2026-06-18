package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.core.enums.BookingSourceEnum
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.profile.PostTabEnum
import com.example.scrollbooker.ui.profile.components.SelectedPostUi

class ProfileNavigator (
    private val navController: NavHostController
) {
    fun toUserProfile(userId: Int, username: String) {
        navController.navigateToUserProfile(userId, username)
    }

    fun toMyPostDetail(postTab: PostTabEnum, selectedPostUi: SelectedPostUi) {
        navController.navigate("${MainRoute.MyProfilePostDetail.route}/${postTab.key}/${selectedPostUi.index}") {
            launchSingleTop = true
        }
    }

    fun toUserPostDetail(postTab: PostTabEnum, selectedPostUi: SelectedPostUi, userId: Int) {
        navController.navigate("${MainRoute.UserProfilePostDetail.route}/${postTab.key}/${selectedPostUi.index}/$userId") {
            launchSingleTop = true
        }
    }

    fun toSocial(socialParam: NavigateSocialParam) {
        navController.navigate(MainRoute.Social.createRoute(socialParam)) {
            launchSingleTop = true
        }
    }

    fun toBookingFromPost(post: Post, source: BookingSourceEnum) {
        navController.navigateToBookingFromPost(post, source)
    }

    fun toBookingFromProduct(product: Product, source: BookingSourceEnum) {
        navController.navigateToBookingFromProduct(product, source)
    }

    fun toBookingFromProfile(params: NavigateBookingParam) {
        navController.navigateToBookingFromProfile(
            businessId = params.businessId,
            userId = params.userId,
            businessOwnerId = params.businessOwnerId,
            source = params.source,
            selectedProductId = params.selectedProductId
        )
    }

    // Edit Profile
    fun toEditProfile(){
        navController.navigate(MainRoute.EditProfile.route) {
            launchSingleTop = true
        }
    }

    fun toEditFullName() {
        navController.navigate(MainRoute.EditFullName.route)
    }
    fun toEditUsername() {
        navController.navigate(MainRoute.EditUsername.route)
    }
    fun toEditBio() {
        navController.navigate(MainRoute.EditBio.route)
    }
    fun toEditGender() {
        navController.navigate(MainRoute.EditGender.route)
    }
    fun toEditBirthdate() {
        navController.navigate(MainRoute.EditBirthDate.route)
    }
    fun toEditAvatarCropScreen() {
        navController.navigate(MainRoute.EditAvatarCropScreen.route)
    }

// V2 - Will be added in next iterations, for now it's hidden in the code

//    fun toEditPublicEmail() {
//        navController.navigate(MainRoute.EditPublicEmail.route)
//    }
//    fun toEditWebsite() {
//        navController.navigate(MainRoute.EditWebsite.route)
//    }

    // My Business
    fun toMyBusiness() {
        navController.navigate(MainRoute.MyBusinessNavigator.route) {
            launchSingleTop = true
        }
    }

    fun toMyBusinessDetails() {
        navController.navigate(MainRoute.MyBusinessDetails.route)
    }
    fun toMySchedules() {
        navController.navigate(MainRoute.MySchedules.route)
    }
    fun toMyProducts() {
        navController.navigate(MainRoute.MyProducts.route)
    }
    fun toMyServices() {
        navController.navigate(MainRoute.MyServices.route)
    }
    fun toMyCalendar() {
        navController.navigate(MainRoute.MyCalendar.route)
    }
    fun toMyEmployees() {
        navController.navigate(MainRoute.MyEmployees.route)
    }

    // Settings
    fun toSettings () {
        navController.navigate(MainRoute.Settings.route) {
            launchSingleTop = true
        }
    }

    fun toDisplay() {
        navController.navigate(MainRoute.Display.route)
    }
    fun toReportProblem() {
        navController.navigate(MainRoute.ReportProblem.route)
    }

    // V2 - Will be added in next iterations, for now it's hidden in the code

//    fun toAccount() {
//        navController.navigate(MainRoute.Account.route)
//    }
//    fun toPrivacy() {
//        navController.navigate(MainRoute.Privacy.route)
//    }
//    fun toSecurity() {
//        navController.navigate(MainRoute.Security.route)
//    }
//    fun toNotifications() {
//        navController.navigate(MainRoute.NotificationSettings.route)
//    }
//    fun toSupport() {
//        navController.navigate(MainRoute.Support.route)
//    }
//    fun toTermsAndConditions() {
//        navController.navigate(MainRoute.TermsAndConditions.route)
//    }

    // Camera
    fun toCamera() {
        navController.navigate(MainRoute.Camera.route) {
            launchSingleTop = true
        }
    }

    fun toCameraGallery() {
        navController.navigate(MainRoute.CameraGallery.route)
    }

    fun toCameraPreview() {
        navController.navigate(MainRoute.CameraPreview.route)
    }

    fun toCreatePost() {
        navController.navigate(MainRoute.CreatePost.route)
    }

    fun toCreatePostPreview() {
        navController.navigate(MainRoute.CreatePostPreview.route)
    }
}
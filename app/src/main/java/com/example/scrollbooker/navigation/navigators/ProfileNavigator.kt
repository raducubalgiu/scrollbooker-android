package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.core.enums.BookingSourceEnum
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.search.businessProfile.ReviewsSheetParams

class ProfileNavigator (
    private val navController: NavHostController
) {
    fun back() {
        navController.popBackStack()
    }

    fun toUserProfile(param: UserProfileParam) {
        navController.navigateToUserProfile(param)
    }

    fun toMyPostDetail(param: ProfilePostDetailParam) {
        val route = MainRoute.MyProfilePostDetail.createRoute(param)

        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    fun toUserPostDetail(param: ProfilePostDetailParam) {
        val route = MainRoute.UserProfilePostDetail.createRoute(param)

        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    fun toEditPost(postId: Int) {
        navController.navigate(MainRoute.EditPostNavigator.createRoute(postId)) {
            launchSingleTop = true
        }
    }

    fun toSocial(socialParam: SocialParam) {
        navController.navigate(MainRoute.Social.createRoute(socialParam)) {
            launchSingleTop = true
        }
    }

    fun toReviews(param: ReviewsSheetParams) {
        navController.navigate(MainRoute.ReviewsNavigator.createRoute(param))
    }

    fun toBookingFromProduct(product: Product, source: BookingSourceEnum) {
        navController.navigateToBookingFromProduct(product, source)
    }

    fun toBookingFromProfile(params: BookingParam) {
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
    fun toMyDashboard() {
        navController.navigate(MainRoute.MyDashboard.route)
    }
    fun toUnapprovedBusinesses() {
        navController.navigate(MainRoute.UnapprovedBusinesses.route)
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
    fun toAddProduct() {
        navController.navigate(MainRoute.AddProduct.route)
    }

    fun toEditProduct(productId: Int) {
        val route = MainRoute.EditProduct.createRoute(productId)

        navController.navigate(route) {
            launchSingleTop = true
        }
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
    fun toEmploymentSelectEmployee() {
        navController.navigate(MainRoute.EmploymentSelectEmployee.route)
    }
    fun toEmploymentAssignJob() {
        navController.navigate(MainRoute.EmploymentAssignJob.route)
    }
    fun toEmploymentAcceptTerms() {
        navController.navigate(MainRoute.EmploymentAcceptTerms.route)
    }

    // Settings
    fun toSettings () {
        navController.navigate(MainRoute.Settings.route)
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
    fun toCamera(cameraParams: CameraParams) {
        navController.navigate(MainRoute.CameraNavigator.createRoute(cameraParams)) {
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

    fun toCreatePostCover() {
        navController.navigate(MainRoute.CreatePostCover.route)
    }
}
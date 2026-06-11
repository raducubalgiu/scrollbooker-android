package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.profile.PostTabEnum
import com.example.scrollbooker.ui.profile.components.SelectedPostUi

class ProfileNavigator (
    private val navController: NavHostController
) {
    fun toCamera() {
        navController.navigate(MainRoute.Camera.route) {
            launchSingleTop = true
        }
    }

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

    fun toMyCalendar() {
        navController.navigate(MainRoute.MyCalendar.route) {
            launchSingleTop = true
        }
    }

    fun toMyBusiness() {
        navController.navigate(MainRoute.MyBusinessNavigator.route) {
            launchSingleTop = true
        }
    }

    fun toEditProfile(){
        navController.navigate(MainRoute.EditProfile.route) {
            launchSingleTop = true
        }
    }

    fun toSettings () {
        navController.navigate(MainRoute.Settings.route) {
            launchSingleTop = true
        }
    }

    fun toSocial(socialParam: NavigateSocialParam) {
        val ( tabIndex, userId, username, isBusinessOrEmployee ) = socialParam

        navController.navigate(
            "${MainRoute.Social.route}/${tabIndex}/${userId}/${username}/${isBusinessOrEmployee}"
        )  {
            launchSingleTop = true
        }
    }

    fun toBookingFromProduct(product: Product, source: String) {
        navController.navigateToBookingFromProduct(product, source)
    }

    fun toBookingFromProfile(
        businessId: Int,
        userId: Int,
        businessOwnerId: Int,
        source: String,
        selectedProductId: Int?
    ) {
        navController.navigateToBookingFromProfile(
            businessId = businessId,
            userId = userId,
            businessOwnerId = businessOwnerId,
            source = source,
            selectedProductId = selectedProductId
        )
    }
}
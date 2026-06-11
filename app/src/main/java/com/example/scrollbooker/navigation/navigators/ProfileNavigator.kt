package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.profile.PostTabEnum
import com.example.scrollbooker.ui.profile.components.SelectedPostUi
import timber.log.Timber

class ProfileNavigator (
    private val navController: NavHostController
) {
    fun toCamera() {
        navController.navigate(MainRoute.Camera.route) {
            launchSingleTop = true
        }
    }

    fun toUserProfile(userId: Int, username: String) {
        navController.navigate("${MainRoute.UserProfile.route}/${userId}/${username}") {
            launchSingleTop = true
        }
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

    fun toBooking(
        businessId: Int,
        userId: Int,
        businessOwnerId: Int,
        source: String,
        selectedProductId: Int?
    ) {
        var route = "bookingNavigator/$businessId/$userId/$businessOwnerId/$source"

        if (selectedProductId != null) {
            route += "?selectedProductId=$selectedProductId"
        }

        navController.navigate(route) {
            launchSingleTop = true
        }
    }
}
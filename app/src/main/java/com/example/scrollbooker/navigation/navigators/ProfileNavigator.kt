package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.navigation.routes.MainRoute

class ProfileNavigator (
    private val navController: NavHostController
) {
    fun toSocial(socialParams: NavigateSocialParam) {
        val ( tabIndex, userId, username, isBusinessOrEmployee ) = socialParams

        navController.navigate(
            "${MainRoute.UserSocial.route}/${tabIndex}/${userId}/${username}/${isBusinessOrEmployee}"
        )
    }
    fun toEditProfile(){
        navController.navigate(MainRoute.EditProfile.route)
    }
    fun toBusinessOwner(ownerId: Int) {
        navController.navigate("${MainRoute.UserProfile.route}/${ownerId}")
    }

    fun toPostDetail() {}

    fun toCalendar (calendarParams: NavigateCalendarParam) {
        val (userId, slotDuration, productId, productName) = calendarParams
        navController.navigate(
            "${MainRoute.Calendar.route}/${userId}/${slotDuration}/${productId}/${productName}"
        )
    }
    fun toMyBusiness() {
        navController.navigate(MainRoute.MyBusinessNavigator.route)
    }
    fun toSettings () {
        navController.navigate(MainRoute.Settings.route)
    }
    fun toCreatePost() {

    }
}
package com.example.scrollbooker.ui.profile.social

sealed class SocialTab(val route: String) {
    object Reviews: SocialTab(route = "Reviews")
    object Bookings: SocialTab(route = "Bookings")
    object Followers: SocialTab(route = "Followers")
    object Followings: SocialTab(route = "Followings")

    companion object {
        fun getTabs(isBusinessOrEmployee: Boolean): List<SocialTab> {
            return buildList {
                if(isBusinessOrEmployee) add(Reviews)
                if(!isBusinessOrEmployee) add(Bookings)
                add(Followers)
                add(Followings)
            }
        }
    }
}
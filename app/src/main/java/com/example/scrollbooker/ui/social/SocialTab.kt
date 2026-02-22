package com.example.scrollbooker.ui.social

sealed class SocialTab(val route: String) {
    object Reviews: SocialTab(route = "Reviews")
    object Followers: SocialTab(route = "Followers")
    object Followings: SocialTab(route = "Followings")

    companion object {
        fun getTabs(): List<SocialTab> {
            return listOf(
                Reviews,
                Followers,
                Followings
            )
        }
    }
}
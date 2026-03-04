package com.example.scrollbooker.ui.social
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.tabs.TabUI

sealed class SocialTab(
    override val route: String,
    override val title: Int
): TabUI {
    object Reviews: SocialTab(route = "Reviews", title = R.string.reviews)
    object Followers: SocialTab(route = "Followers", title = R.string.followers)
    object Followings: SocialTab(route = "Followings", title = R.string.following)

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
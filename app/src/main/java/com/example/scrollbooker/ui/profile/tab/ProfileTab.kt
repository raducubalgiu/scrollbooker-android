package com.example.scrollbooker.ui.profile.tab

import com.example.scrollbooker.R

sealed class ProfileTab(
    val route: String,
    val icon: Int
) {
    object Posts: ProfileTab(route = "Posts", icon = R.drawable.ic_video_outline)
    object Products: ProfileTab(route = "Products", icon = R.drawable.ic_shopping_outline)
    object Reposts: ProfileTab(route = "Reposts", icon = R.drawable.ic_arrow_repeat_outline)
    object Bookmarks: ProfileTab(route = "Bookmarks", icon = R.drawable.ic_bookmark_outline)
    object Info: ProfileTab(route = "Info", icon = R.drawable.ic_location_outline)

    companion object {
        fun getTabs(isBusinessOrEmployee: Boolean): List<ProfileTab> {
            return buildList {
                add(Posts)
                if(isBusinessOrEmployee) add(Products)
                add(Reposts)
                add(Bookmarks)
                if(isBusinessOrEmployee) add(Info)
            }
        }
    }
}
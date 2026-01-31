package com.example.scrollbooker.ui.profile.tabs

import com.example.scrollbooker.R

sealed class ProfileTab(
    val route: String,
    val icon: Int
) {
    object Posts: ProfileTab(route = "Posts", icon = R.drawable.ic_video_outline)
    object Products: ProfileTab(route = "Products", icon = R.drawable.ic_shopping_outline)
    object Employees: ProfileTab(route = "Employees", icon = R.drawable.ic_users_outline)
    object Bookmarks: ProfileTab(route = "Bookmarks", icon = R.drawable.ic_bookmark_outline)
    object Info: ProfileTab(route = "Info", icon = R.drawable.ic_location_outline)

    companion object {
        fun getTabs(isBusinessOrEmployee: Boolean, isMyProfile: Boolean): List<ProfileTab> {
            return buildList {
                if(isBusinessOrEmployee) add(Posts)
                if(isBusinessOrEmployee) add(Products)
                if(isBusinessOrEmployee) add(Employees)
                if(isMyProfile) add(Bookmarks)
                if(isBusinessOrEmployee) add(Info)
            }
        }
    }
}
package com.example.scrollbooker.core.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.scrollbooker.R

sealed class BottomNavItem(
    val route: String,
    @DrawableRes val icon: Int,
    @StringRes val label: Int
) {
    object Feed: BottomNavItem("feed", R.drawable.ic_home, R.string.feed)
    object Inbox: BottomNavItem("inbox", R.drawable.ic_notifications, R.string.inbox)
    object Search: BottomNavItem("search", R.drawable.ic_search, R.string.search)
    object Appointments: BottomNavItem("appointments", R.drawable.ic_calendar, R.string.appointments)
    object Profile: BottomNavItem("profile", R.drawable.ic_person, R.string.profile)

    companion object {
        val items = listOf(Feed, Inbox, Search, Appointments, Profile)
    }
}
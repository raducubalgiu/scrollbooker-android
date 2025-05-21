package com.example.scrollbooker.core.nav.routes

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.scrollbooker.R

sealed class MainRoute(
    val route: String,
    @DrawableRes val icon: Int,
    @StringRes val label: Int
) {
    object Feed: MainRoute(route = "feed", R.drawable.ic_home, R.string.feed)
    object Inbox: MainRoute(route = "inbox", R.drawable.ic_notifications, R.string.inbox)
    object Search: MainRoute(route = "search", R.drawable.ic_search, R.string.search)
    object Appointments: MainRoute(route = "appointments", R.drawable.ic_calendar, R.string.appointments)
    object Profile: MainRoute(route = "profile", R.drawable.ic_person, R.string.profile)
}
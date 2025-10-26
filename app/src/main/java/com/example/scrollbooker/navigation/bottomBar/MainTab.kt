package com.example.scrollbooker.navigation.bottomBar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.scrollbooker.R
import com.example.scrollbooker.navigation.routes.MainRoute

sealed class MainTab(
    val route: String,
    @StringRes val label: Int,
    @DrawableRes val painterSolid: Int,
    @DrawableRes val painterOutline: Int
) {
    object Feed: MainTab(
        route = MainRoute.Feed.route,
        label = R.string.home,
        painterSolid = R.drawable.ic_home_solid,
        painterOutline = R.drawable.ic_home_outline,
    )
    object Inbox: MainTab(
        route = MainRoute.Inbox.route,
        label = R.string.inbox,
        painterSolid = R.drawable.ic_notifications_solid,
        painterOutline = R.drawable.ic_notifications_outline
    )
    object Search: MainTab(
        route = MainRoute.Search.route,
        label = R.string.search,
        painterSolid = R.drawable.ic_search_solid,
        painterOutline = R.drawable.ic_search
    )
    object Appointments: MainTab(
        route = MainRoute.Appointments.route,
        label = R.string.bookings,
        painterSolid = R.drawable.ic_clipboard_solid,
        painterOutline = R.drawable.ic_clipboard_outline
    )
    object Profile: MainTab(
        route = MainRoute.MyProfile.route,
        label = R.string.profile,
        painterSolid = R.drawable.ic_person_outline,
        painterOutline = R.drawable.ic_person_outline
    )

    companion object {
        fun fromRoute(route: String?): MainTab =
            route?.let(::fromRoute) ?: Feed

        val allTabs = listOf(Feed, Inbox, Search, Appointments, Profile)
    }
}
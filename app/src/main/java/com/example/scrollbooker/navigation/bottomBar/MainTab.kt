package com.example.scrollbooker.navigation.bottomBar

import androidx.annotation.DrawableRes
import com.example.scrollbooker.R
import com.example.scrollbooker.navigation.routes.MainRoute

sealed class MainTab(
    val route: String,
    val label: String,
    @DrawableRes val painterSolid: Int,
    @DrawableRes val painterOutline: Int
) {
    object Feed: MainTab(
        MainRoute.Feed.route, "Acasa",
        painterSolid = R.drawable.ic_home_solid,
        painterOutline = R.drawable.ic_home_outline,
    )
    object Inbox: MainTab(
        MainRoute.Inbox.route, "Inbox",
        painterSolid = R.drawable.ic_notifications_solid,
        painterOutline = R.drawable.ic_notifications_outline
    )
    object Search: MainTab(
        MainRoute.Search.route, "Search",
        painterSolid = R.drawable.ic_search_solid,
        painterOutline = R.drawable.ic_search
    )
    object Appointments: MainTab(
        MainRoute.Appointments.route, "Comenzi",
        painterSolid = R.drawable.ic_clipboard_solid,
        painterOutline = R.drawable.ic_clipboard_outline
    )
    object Profile: MainTab(
        MainRoute.MyProfile.route, "Profil",
        painterSolid = R.drawable.ic_person_solid,
        painterOutline = R.drawable.ic_person_outline
    )

    companion object {
        fun fromRoute(route: String?): MainTab =
            route?.let(::fromRoute) ?: Feed

        val allTabs = listOf(Feed, Inbox, Search, Appointments, Profile)
    }
}
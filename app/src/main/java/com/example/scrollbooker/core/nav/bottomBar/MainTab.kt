package com.example.scrollbooker.core.nav.bottomBar

import com.example.scrollbooker.R
import com.example.scrollbooker.core.nav.routes.MainRoute

sealed class MainTab(
    val route: String,
    val label: String,
    val painter: Int
) {
    object Feed: MainTab(
        MainRoute.Feed.route, "Acasa",
        painter = R.drawable.ic_home_solid
    )
    object Inbox: MainTab(
        MainRoute.Inbox.route, "Inbox",
        painter = R.drawable.ic_notifications_solid
    )
    object Search: MainTab(
        MainRoute.Search.route, "Search",
        painter = R.drawable.ic_search_solid
    )
    object Appointments: MainTab(
        MainRoute.Appointments.route, "Comenzi",
        painter = R.drawable.ic_clipboard_solid
    )
    object Profile: MainTab(
        MainRoute.MyProfile.route, "Profil",
        painter = R.drawable.ic_person_solid
    )

    companion object {
        fun fromRoute(route: String): MainTab = when(route) {
            Feed.route -> Feed
            Inbox.route -> Inbox
            Search.route -> Search
            Appointments.route -> Appointments
            Profile.route -> Profile
            else -> Feed
        }

        val allTabs = listOf(Feed, Inbox, Search, Appointments, Profile)
    }
}
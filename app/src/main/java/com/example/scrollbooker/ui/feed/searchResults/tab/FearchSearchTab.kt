package com.example.scrollbooker.ui.feed.searchResults.tab

sealed class FeedSearchTab(val route: String) {
    object ForYou: FeedSearchTab(route = "For You")
    object Users: FeedSearchTab(route = "Users")
    object LastMinute: FeedSearchTab(route = "Last Minute")
    object InstantBooking: FeedSearchTab(route = "Instant Booking")

    companion object {
        fun fromRoute(route: String): FeedSearchTab = when(route) {
            ForYou.route -> ForYou
            Users.route -> Users
            LastMinute.route -> LastMinute
            InstantBooking.route -> InstantBooking
            else -> ForYou
        }

        val getTabs = listOf(ForYou, Users, LastMinute, InstantBooking)
    }
}
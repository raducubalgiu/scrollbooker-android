package com.example.scrollbooker.ui.myBusiness.myDashboard.tabs

import com.example.scrollbooker.R
import androidx.annotation.StringRes

sealed class MyDashboardTab(
    val route: String,
    @StringRes val labelRes: Int
) {
    object MyAppointments : MyDashboardTab(
        route = "appointments",
        labelRes = R.string.appointments
    )

    object MyPosts : MyDashboardTab(
        route = "posts",
        labelRes = R.string.posts
    )

    companion object {
        fun getTabs(): List<MyDashboardTab> {
            return listOf(MyAppointments, MyPosts)
        }
    }
}

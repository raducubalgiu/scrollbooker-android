package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient

sealed class OwnClientTab(val route: String) {
    object CreateOwnClient: OwnClientTab(route = "Client Propriu")
    object LastMinute: OwnClientTab(route = "Last Minute")

    companion object {
        val getTabs = listOf(CreateOwnClient, LastMinute)
    }
}
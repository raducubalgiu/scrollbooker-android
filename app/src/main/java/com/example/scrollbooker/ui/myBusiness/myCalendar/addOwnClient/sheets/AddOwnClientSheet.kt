package com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets

sealed interface AddOwnClientSheet {
    data object SelectClient: AddOwnClientSheet
    data object AddClient: AddOwnClientSheet
    data object Services: AddOwnClientSheet
    data object DateTime: AddOwnClientSheet
}

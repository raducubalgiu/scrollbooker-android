package com.example.scrollbooker.components.core.tabs

import androidx.annotation.StringRes

interface TabUI {
    val route: String
    @get:StringRes val title: Int
}
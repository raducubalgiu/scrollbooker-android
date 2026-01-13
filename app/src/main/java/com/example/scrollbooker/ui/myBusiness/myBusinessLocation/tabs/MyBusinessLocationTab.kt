package com.example.scrollbooker.ui.myBusiness.myBusinessLocation.tabs

import androidx.annotation.StringRes
import com.example.scrollbooker.R

sealed class MyBusinessLocationTab(
    val route: String,
    @StringRes val label: Int,
) {
    object Summary: MyBusinessLocationTab(
        route = "Summary",
        label = R.string.summary
    )
    object Gallery: MyBusinessLocationTab(
        route = "Gallery",
        label = R.string.photoGallery
    )

    companion object {
        fun fromRoute(route: String): MyBusinessLocationTab = when(route) {
            Summary.route -> Summary
            Gallery.route -> Gallery
            else -> Summary
        }

        val getTabs = listOf(Summary, Gallery)
    }
}
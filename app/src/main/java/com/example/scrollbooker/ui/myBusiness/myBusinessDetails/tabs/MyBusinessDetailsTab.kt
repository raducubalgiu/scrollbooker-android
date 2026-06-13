package com.example.scrollbooker.ui.myBusiness.myBusinessDetails.tabs

import androidx.annotation.StringRes
import com.example.scrollbooker.R

sealed class MyBusinessDetailsTab(
    val route: String,
    @StringRes val label: Int,
) {
    object Summary: MyBusinessDetailsTab(
        route = "Summary",
        label = R.string.summary
    )
    object Gallery: MyBusinessDetailsTab(
        route = "Gallery",
        label = R.string.photoGallery
    )

    companion object {
        fun fromRoute(route: String): MyBusinessDetailsTab = when(route) {
            Summary.route -> Summary
            Gallery.route -> Gallery
            else -> Summary
        }

        val getTabs = listOf(Summary, Gallery)
    }
}
package com.example.scrollbooker.ui.search.businessProfile

import androidx.annotation.StringRes
import com.example.scrollbooker.R

sealed class BusinessProfileSection(
    @StringRes val label: Int,
    val key: String
) {
    object Photos: BusinessProfileSection(R.string.photos, "photos")
    object Services: BusinessProfileSection(R.string.services, "services")
    object Social: BusinessProfileSection(R.string.social, "social")
    object Employees: BusinessProfileSection(R.string.employees, "employees")
    object Reviews: BusinessProfileSection(R.string.reviews, "reviews")
    object About: BusinessProfileSection(R.string.about, "about")

    companion object {
        val all = listOf(Photos, Services, Social, Employees, Reviews, About)
    }
}
package com.example.scrollbooker.ui.search.businessProfile.sections

import androidx.annotation.StringRes
import com.example.scrollbooker.R
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfileEmployee

sealed class BusinessProfileSection(
    @StringRes val label: Int,
    val key: String
) {
    object Summary: BusinessProfileSection(R.string.summary, "summary")
    object Services: BusinessProfileSection(R.string.services, "services")
    object Social: BusinessProfileSection(R.string.social, "social")
    object Employees: BusinessProfileSection(R.string.employees, "employees")
    object Reviews: BusinessProfileSection(R.string.reviews, "reviews")
    object About: BusinessProfileSection(R.string.about, "about")

    companion object {
        val all = listOf(Summary, Services, Social, Employees, Reviews, About)

        fun getSections(
            employees: List<BusinessProfileEmployee>
        ): List<BusinessProfileSection> {
            return buildList {
                add(Summary)
                add(Services)
                add(Social)
                if(employees.isNotEmpty()) add(Employees)
                add(Reviews)
                add(About)
            }
        }
    }
}
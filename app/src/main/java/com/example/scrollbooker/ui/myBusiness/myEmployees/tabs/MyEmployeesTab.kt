package com.example.scrollbooker.ui.myBusiness.myEmployees.tabs

import androidx.annotation.StringRes
import com.example.scrollbooker.R

sealed class MyEmployeesTab(
    val route: String,
    @StringRes val label: Int,
) {
    object Employees: MyEmployeesTab(
        route = "employees",
        label = R.string.employees
    )
    object EmploymentRequests: MyEmployeesTab(
        route = "employmentRequests",
        label = R.string.employmentRequests
    )

    companion object {
        fun fromRoute(route: String): MyEmployeesTab = when(route) {
            Employees.route -> Employees
            EmploymentRequests.route -> EmploymentRequests
            else -> Employees
        }

        val getTabs = listOf(Employees, EmploymentRequests)
    }
}
package com.example.scrollbooker.ui.appointments.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R

enum class AppointmentFilterTitleEnum(val key: String) {
    ALL("all"),
    AS_EMPLOYEE("asEmployee"),
    AS_CLIENT("asClient");

    @Composable
    fun getLabel(): String {
        return when(this) {
            ALL -> stringResource(R.string.all)
            AS_EMPLOYEE -> stringResource(R.string.asEmployee)
            AS_CLIENT -> stringResource(R.string.asClient)
        }
    }
}

data class AppointmentFilter(
    val title: AppointmentFilterTitleEnum,
    val asCustomer: Boolean?
)

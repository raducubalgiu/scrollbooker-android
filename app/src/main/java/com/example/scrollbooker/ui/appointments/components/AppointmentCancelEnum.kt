package com.example.scrollbooker.ui.appointments.components

import androidx.annotation.StringRes
import com.example.scrollbooker.R

enum class AppointmentCancelEnum(val key: String) {
    CANNOT_ARRIVE("cannotArriveToAppointment"),
    FOUND_BETTER_OFFER("iFoundABetterOffer"),
    BOOKED_BY_MISTAKE("iBookedByMistake"),
    OTHER_REASON("otherReason");

    companion object {
        fun fromKey(key: String): AppointmentCancelEnum? =
            AppointmentCancelEnum.entries.find { it.key == key }

        @StringRes
        fun label(enum: AppointmentCancelEnum?): Int = when(enum) {
            CANNOT_ARRIVE -> R.string.cannotArriveToAppointment
            FOUND_BETTER_OFFER -> R.string.foundABetterOffer
            BOOKED_BY_MISTAKE -> R.string.bookedByMistake
            OTHER_REASON -> R.string.otherReason
            null -> R.string.otherReason
        }
    }
}
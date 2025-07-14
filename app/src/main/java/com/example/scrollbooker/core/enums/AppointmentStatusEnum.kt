package com.example.scrollbooker.core.enums
import androidx.compose.ui.graphics.Color
import com.example.scrollbooker.R

enum class AppointmentStatusEnum(val key: String) {
    IN_PROGRESS("in_progress"),
    FINISHED("finished"),
    CANCELED("canceled");

    companion object {
        fun fromKey(key: String): AppointmentStatusEnum? =
            AppointmentStatusEnum.entries.find { it.key == key }

        fun fromKeys(keys: List<String>): List<AppointmentStatusEnum> =
            keys.mapNotNull { AppointmentStatusEnum.Companion.fromKey(it) }

        fun getLabelRes(status: AppointmentStatusEnum): Int {
            return when(status) {
                IN_PROGRESS -> R.string.confirmed
                FINISHED -> R.string.finished
                CANCELED -> R.string.canceled
            }
        }

        fun getLabel(statusKey: String): Int? {
            return fromKey(statusKey)?.let { getLabelRes(it) }
        }

        fun getColor(status: AppointmentStatusEnum): Color {
            return when(status) {
                IN_PROGRESS -> Color(0xFF66BB6A)
                FINISHED -> Color.Gray
                CANCELED -> Color.Red
            }
        }

        fun getColor(statusKey: String): Color? {
            return fromKey(statusKey)?.let { getColor(it) }
        }
    }
}
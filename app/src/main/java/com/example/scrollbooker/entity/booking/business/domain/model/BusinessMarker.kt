package com.example.scrollbooker.entity.booking.business.domain.model

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.scrollbooker.core.enums.AppointmentStatusEnum.CANCELED
import com.example.scrollbooker.core.enums.AppointmentStatusEnum.FINISHED
import com.example.scrollbooker.core.enums.AppointmentStatusEnum.IN_PROGRESS
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.model.BusinessCoordinates

data class BusinessMarker(
    val business: BusinessSummary,
    val businessShortDomain: String,
    val address: String,
    val coordinates: BusinessCoordinates,
    val isPrimary: Boolean,
    val hasVideo: Boolean,
    val mediaPreview: BusinessMediaPreview?,
)

data class BusinessMediaPreview(
    val type: String,
    val thumbnailUrl: String?,
    val previewVideoUrl: String?
)

fun BusinessMarker.getMarkerColor(): Color =
    when(businessShortDomain) {
        "Beauty" -> Color(0xFFFF6F00)
        "Auto" -> Color(0xFF3A86FF)
        "Medical" -> Color(0xFF36CFC9)
        else -> Color.Gray
    }

fun BusinessMarker.getMarkerGradient(): Brush {
    val base = getMarkerColor()
    return Brush.radialGradient(
        colors = listOf(
            base.copy(alpha = 0.1f),
            base
        )
    )
}
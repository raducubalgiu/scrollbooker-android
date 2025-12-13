package com.example.scrollbooker.entity.booking.business.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.scrollbooker.entity.booking.appointment.domain.model.BusinessCoordinates
import com.example.scrollbooker.ui.theme.Auto
import com.example.scrollbooker.ui.theme.Beauty
import com.example.scrollbooker.ui.theme.Medical

data class BusinessMarker(
    val owner: BusinessOwner,
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

@Composable
fun BusinessMarker.getMarkerColor(): Color =
    when(businessShortDomain) {
        "Beauty" -> Beauty
        "Auto" -> Auto
        "Medical" -> Medical
        else -> Color.Gray
    }
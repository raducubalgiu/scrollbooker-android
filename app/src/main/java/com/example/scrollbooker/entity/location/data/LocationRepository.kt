package com.example.scrollbooker.entity.location.data

import com.example.scrollbooker.ui.GeoPoint
import com.example.scrollbooker.ui.PrecisionMode
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun locationUpdates(precisionMode: PrecisionMode): Flow<GeoPoint>
}
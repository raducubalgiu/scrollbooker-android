package com.example.scrollbooker.ui.search

import androidx.lifecycle.ViewModel
import com.example.scrollbooker.entity.nomenclature.businessType.domain.model.BusinessType
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(): ViewModel() {

    private val _viewportState = MapViewportState()
    val viewportState: MapViewportState = _viewportState

    fun setViewportState(center: Point, zoom: Double = 10.0) {
        viewportState.setCameraOptions {
            center(center)
            zoom(zoom)
            pitch(0.0)
            bearing(0.0)
        }
    }

    private val _selectedBusinessType = MutableStateFlow<BusinessType?>(null)
    val selectedBusinessType: StateFlow<BusinessType?> = _selectedBusinessType

    fun setBusinessType(businessType: BusinessType?) {
        _selectedBusinessType.value = businessType
    }

    init {
        setViewportState(center = Point.fromLngLat(26.104, 44.437))
    }
}
package com.example.scrollbooker.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.scrollbooker.entity.nomenclature.businessType.domain.model.BusinessType
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedState: SavedStateHandle
): ViewModel() {

    // Camera persistata cu SavedStateHandle
    val latFlow = savedState.getStateFlow("lat", 44.437f)
    val lonFlow = savedState.getStateFlow("lon", 26.096f)
    val zoomFlow = savedState.getStateFlow("zoom", 12f)

    val isMapReady = savedState.getStateFlow("mapReady", false)

    fun setCamera(lat: Float = latFlow.value, lon: Float = lonFlow.value, zoom: Float = zoomFlow.value) {
        savedState["lat"] = lat
        savedState["lon"] = lon
        savedState["zoom"] = zoom
    }

    fun setMapReady(ready: Boolean) {
        savedState["mapReady"] = ready
    }
}
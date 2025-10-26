package com.example.scrollbooker.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.scrollbooker.entity.nomenclature.businessType.domain.model.BusinessType
import com.google.android.gms.maps.model.CameraPosition
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class CameraPositionState(
    val latitude: Double = 44.4268,
    val longitude: Double = 26.1025,
    val zoom: Double = 12.0,
    val bearing: Double = 0.0,
    val pitch: Double = 0.0
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedState: SavedStateHandle
): ViewModel() {

    private val _cameraPosition = MutableStateFlow<CameraPositionState>(CameraPositionState())
    val cameraPosition: StateFlow<CameraPositionState> = _cameraPosition.asStateFlow()

    private val _isMapReady = MutableStateFlow<Boolean>(false)
    val isMapReady: StateFlow<Boolean> = _isMapReady.asStateFlow()

    private val _isStyleLoaded = MutableStateFlow<Boolean>(false)
    val isStyleLoaded: StateFlow<Boolean> = _isStyleLoaded.asStateFlow()

    fun setMapReady(isReady: Boolean) {
        _isMapReady.value = isReady
    }

    fun setStyleLoaded(isLoaded: Boolean) {
        _isStyleLoaded.value = isLoaded
    }

    fun updateCamera(camera: CameraPositionState) {
        _cameraPosition.value = camera
    }

//    // Camera persistata cu SavedStateHandle
//    val latFlow = savedState.getStateFlow("lat", 44.437f)
//    val lonFlow = savedState.getStateFlow("lon", 26.096f)
//    val zoomFlow = savedState.getStateFlow("zoom", 12f)
//
//    fun setCamera(lat: Float = latFlow.value, lon: Float = lonFlow.value, zoom: Float = zoomFlow.value) {
//        savedState["lat"] = lat
//        savedState["lon"] = lon
//        savedState["zoom"] = zoom
//    }

//    data class MapCamera(
//        val lat: Double = 44.4268,
//        val lng: Double = 26.1025,
//        val zoom: Float = 12f,
//        val tilt: Float = 0f,
//        val bearing: Float = 0f
//    )
//
//    private val _camera = MutableStateFlow(MapCamera())
//    val camera: StateFlow<MapCamera> = _camera.asStateFlow()
//
//    private val _isMapLoaded = MutableStateFlow<Boolean>(false)
//    val isMapLoaded: StateFlow<Boolean> = _isMapLoaded.asStateFlow()
//
//    fun onMapLoaded() {
//        _isMapLoaded.value = true
//    }
//
//    fun updateFromMap(position: CameraPosition) {
//        _camera.value = MapCamera(
//            lat = position.target.latitude,
//            lng = position.target.longitude,
//            zoom = position.zoom,
//            tilt = position.tilt,
//            bearing = position.bearing
//        )
//    }
//
//    fun moveTo(lat: Double, lng: Double, zoom: Float = 15f) {
//        _camera.value = _camera.value.copy(lat = lat, lng = lng, zoom = zoom)
//    }
}
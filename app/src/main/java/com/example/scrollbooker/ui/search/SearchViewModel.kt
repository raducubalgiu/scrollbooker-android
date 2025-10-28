package com.example.scrollbooker.ui.search
import androidx.lifecycle.ViewModel
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
class SearchViewModel @Inject constructor(): ViewModel() {
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

    private val _maximumDistance = MutableStateFlow<Float>(50f)
    val maximumDistance: StateFlow<Float> = _maximumDistance.asStateFlow()

    private val _maximumPrice = MutableStateFlow<Float>(1400f)
    val maximumPrice: StateFlow<Float> = _maximumPrice.asStateFlow()

    private val _distance = MutableStateFlow<Float>(50f)
    val distance: StateFlow<Float> = _distance.asStateFlow()

    private val _price = MutableStateFlow<Float>(1400f)
    val price: StateFlow<Float> = _price.asStateFlow()

    fun setDistance(distance: Float) {
        _distance.value = distance
    }

    fun setPrice(price: Float) {
        _price.value = price
    }
}
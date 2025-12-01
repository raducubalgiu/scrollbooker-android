package com.example.scrollbooker.ui.search
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.PaginatedResponseDto
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessBoundingBox
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessMarkersRequest
import com.example.scrollbooker.entity.booking.business.domain.model.Business
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMarker
import com.example.scrollbooker.entity.booking.business.domain.useCase.GetBusinessesMarkersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class CameraPositionState(
    val latitude: Double = 44.4268,
    val longitude: Double = 26.1025,
    val zoom: Double = 10.0,
    val bearing: Double = 0.0,
    val pitch: Double = 0.0,
)

data class MarkersUiState(
    val data: PaginatedResponseDto<BusinessMarker>? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getBusinessesMarkersUseCase: GetBusinessesMarkersUseCase
): ViewModel() {
    private val _markersUiState = MutableStateFlow(MarkersUiState())
    val markersUiState: StateFlow<MarkersUiState> = _markersUiState.asStateFlow()

    private val _cameraPosition = MutableStateFlow<CameraPositionState>(CameraPositionState())
    val cameraPosition: StateFlow<CameraPositionState> = _cameraPosition.asStateFlow()

    private val _bBox = MutableStateFlow<BusinessBoundingBox?>(null)

    private val _isMapReady = MutableStateFlow<Boolean>(false)
    val isMapReady: StateFlow<Boolean> = _isMapReady.asStateFlow()

    private val _isStyleLoaded = MutableStateFlow<Boolean>(false)
    val isStyleLoaded: StateFlow<Boolean> = _isStyleLoaded.asStateFlow()

    private val _maximumDistance = MutableStateFlow<Float>(50f)
    val maximumDistance: StateFlow<Float> = _maximumDistance.asStateFlow()

    private val _maximumPrice = MutableStateFlow<Float>(1400f)
    val maximumPrice: StateFlow<Float> = _maximumPrice.asStateFlow()

    private val _distance = MutableStateFlow<Float>(50f)
    val distance: StateFlow<Float> = _distance.asStateFlow()

    private val _price = MutableStateFlow<Float>(1400f)
    val price: StateFlow<Float> = _price.asStateFlow()

    private var fetchMarkersJob: Job? = null

    @OptIn(FlowPreview::class)
    private fun fetchMarkers(BBox: BusinessBoundingBox) {
        fetchMarkersJob?.cancel()

        fetchMarkersJob = viewModelScope.launch {
            _markersUiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            val zoom = cameraPosition.value.zoom
            val request = BusinessMarkersRequest(
                bbox = BBox,
                zoom = zoom.toFloat(),
                maxMarkers = 400
            )
            val result = withVisibleLoading { getBusinessesMarkersUseCase(request) }

            when(result) {
                is FeatureState.Success -> {
                    _markersUiState.update {
                        it.copy(
                            data = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    fun setBBox(newBBox: BusinessBoundingBox) {
        val old = _bBox.value
        if(old == newBBox) return
        _bBox.value = newBBox
        fetchMarkers(newBBox)
    }

    fun setMapReady(isReady: Boolean) {
        _isMapReady.value = isReady
    }

    fun setStyleLoaded(isLoaded: Boolean) {
        _isStyleLoaded.value = isLoaded
    }

    fun updateCamera(camera: CameraPositionState) {
        _cameraPosition.value = camera
    }

    fun setDistance(distance: Float) {
        _distance.value = distance
    }

    fun setPrice(price: Float) {
        _price.value = price
    }
}
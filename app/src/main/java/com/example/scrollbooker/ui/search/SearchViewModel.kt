package com.example.scrollbooker.ui.search
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.enums.SearchSortEnum
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.PaginatedResponseDto
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.appointment.domain.model.BusinessCoordinates
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessBoundingBox
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessMarkersRequest
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMarker
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessSheet
import com.example.scrollbooker.entity.booking.business.domain.useCase.GetBusinessesMarkersUseCase
import com.example.scrollbooker.entity.booking.business.domain.useCase.GetBusinessesSheetUseCase
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.useCase.GetAllBusinessDomainsUseCase
import com.example.scrollbooker.entity.nomenclature.businessType.domain.model.BusinessType
import com.example.scrollbooker.entity.nomenclature.businessType.domain.useCase.GetAllBusinessTypesByBusinessDomainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

data class SheetUiState(
    val isLoading: Boolean = false,
    val error: Throwable? = null
)

data class SearchFiltersState(
    val businessDomainId: Int? = null,
    val businessTypeId: Int? = null,
    val serviceId: Int? = null,
    val subFilterIds: List<Int> = emptyList(),
    val maxDistance: Float? = 1500f,
    val maxPrice: Float? = null,
    val sort: SearchSortEnum = SearchSortEnum.RECOMMENDED,
    val isLastMinute: Boolean = false,
    val hasDiscount: Boolean = false,
    val hasVideo: Boolean = false
)

data class SearchRequestState(
    val bBox: BusinessBoundingBox? = null,
    val zoom: Float = 12f,
    val userLocation: BusinessCoordinates? = null,
    val filters: SearchFiltersState = SearchFiltersState()
) {
    fun toRequest(): BusinessMarkersRequest? {
        val box = bBox ?: return null

        return BusinessMarkersRequest(
            bbox = box,
            zoom = zoom,
            maxMarkers = 400,
            businessDomainId = filters.businessDomainId,
            businessTypeId = filters.businessTypeId,
            serviceId = filters.serviceId,
            subFilterIds = filters.subFilterIds,
            userLocation = userLocation,
            maxPrice = filters.maxPrice,
            sort = filters.sort.raw,
            hasDiscount = filters.hasDiscount,
            isLastMinute = filters.isLastMinute,
            hasVideo = filters.hasVideo
        )
    }
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getBusinessesMarkersUseCase: GetBusinessesMarkersUseCase,
    private val getBusinessesSheetUseCase: GetBusinessesSheetUseCase,
    private val getAllBusinessDomainsUseCase: GetAllBusinessDomainsUseCase,
    private val getAllBusinessTypesByBusinessDomainUseCase: GetAllBusinessTypesByBusinessDomainUseCase
): ViewModel() {
    private val _request = MutableStateFlow(SearchRequestState())
    val request: StateFlow<SearchRequestState> = _request.asStateFlow()

    private val _businessDomains = MutableStateFlow<FeatureState<List<BusinessDomain>>>(FeatureState.Loading)
    val businessDomains: StateFlow<FeatureState<List<BusinessDomain>>> = _businessDomains.asStateFlow()

    private val _markersUiState = MutableStateFlow(MarkersUiState())
    val markersUiState: StateFlow<MarkersUiState> = _markersUiState.asStateFlow()

    private val _sheetUiState = MutableStateFlow(SheetUiState())

    private val _isMapReady = MutableStateFlow<Boolean>(false)
    val isMapReady: StateFlow<Boolean> = _isMapReady.asStateFlow()

    private val _isStyleLoaded = MutableStateFlow<Boolean>(false)
    val isStyleLoaded: StateFlow<Boolean> = _isStyleLoaded.asStateFlow()

    private val _cameraPosition = MutableStateFlow<CameraPositionState>(CameraPositionState())
    val cameraPosition: StateFlow<CameraPositionState> = _cameraPosition.asStateFlow()

    private val rawRequestFlow: Flow<BusinessMarkersRequest> =
        _request
            .mapNotNull { it.toRequest() }
            .debounce(100L)
            .distinctUntilChanged()

    private val sharedRequestFlow = rawRequestFlow
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            replay = 1
        )

    init {
        sharedRequestFlow
            .flatMapLatest { req ->
                flow {
                    _markersUiState.update { current ->
                        current.copy(isLoading=true, error = null)
                    }

                    val result = withVisibleLoading { getBusinessesMarkersUseCase(req) }

                    when(result) {
                        is FeatureState.Success -> _markersUiState.update { current ->
                            current.copy(
                                data = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                        is FeatureState.Error ->  _markersUiState.update { current ->
                            current.copy(
                                data = null,
                                isLoading = false,
                                error = result.error
                            )
                        }
                        else -> Unit
                    }

                    emit(Unit)
                }
            }
            .launchIn(viewModelScope)

        sharedRequestFlow
            .onEach { _sheetUiState.value = SheetUiState(isLoading = true, error = null) }
            .launchIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val sheetPagingFlow: Flow<PagingData<BusinessSheet>> =
        sharedRequestFlow
            .flatMapLatest { req ->
                getBusinessesSheetUseCase(req)
            }
            .cachedIn(viewModelScope)

    val businessTypes: StateFlow<FeatureState<List<BusinessType>>?> =
        sharedRequestFlow
            .map { req -> req.businessDomainId }
            .filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest { domainId ->
                flow {
                    emit(FeatureState.Loading)

                    val result = withVisibleLoading {
                        getAllBusinessTypesByBusinessDomainUseCase(domainId)
                    }

                    emit(result)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = FeatureState.Loading
            )

    fun loadAllBusinessDomains() {
        viewModelScope.launch {
            _businessDomains.value = FeatureState.Loading
            _businessDomains.value = withVisibleLoading { getAllBusinessDomainsUseCase() }
        }
    }

    init {
        loadAllBusinessDomains()
    }

    fun updateCamera(position: CameraPositionState) {
        _cameraPosition.value = position
    }

    fun onMapIdle(bBox: BusinessBoundingBox) {
        val zoom = _cameraPosition.value.zoom.toFloat()

        _request.update { current ->
            if(current.bBox == bBox && current.zoom == zoom) {
                current
            } else {
                current.copy(
                    bBox = bBox,
                    zoom = zoom
                )
            }
        }
    }

    fun setMapReady(isReady: Boolean) {
        _isMapReady.value = isReady
    }

    fun setStyleLoaded(isLoaded: Boolean) {
        _isStyleLoaded.value = isLoaded
    }

    fun setBusinessDomain(domainId: Int?) {
        _request.update { current ->
            current.copy(
                filters = current.filters.copy(
                    businessDomainId = domainId
                )
            )
        }
    }

    fun setFiltersFromServicesSheet(
        newBusinessDomain: Int?,
        newServiceId: Int?
    ) {
        _request.update { current ->
            current.copy(
                filters = current.filters.copy(
                    businessDomainId = newBusinessDomain,
                    serviceId = newServiceId
                )
            )
        }
    }

    fun setFiltersFromFiltersSheet(
        maxPrice: Float?,
        sort: SearchSortEnum,
        hasDiscount: Boolean,
        isLastMinute: Boolean,
        hasVideo: Boolean
    ) {
        _request.update { current ->
            current.copy(
                filters = current.filters.copy(
                    maxPrice = maxPrice,
                    sort = sort,
                    hasDiscount = hasDiscount,
                    isLastMinute = isLastMinute,
                    hasVideo = hasVideo
                )
            )
        }
    }

    fun clearFiltersFromFiltersSheet() {
        _request.update { current ->
            current.copy(
                filters = current.filters.copy(
                    maxPrice = 1500f,
                    sort = SearchSortEnum.RECOMMENDED,
                    hasDiscount = false,
                    isLastMinute = false,
                    hasVideo = false
                )
            )
        }
    }
}
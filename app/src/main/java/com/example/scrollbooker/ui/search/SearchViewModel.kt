package com.example.scrollbooker.ui.search
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.enums.SearchSortEnum
import com.example.scrollbooker.core.extensions.toPrettyTime
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.PaginatedResponseDto
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.appointment.domain.model.BusinessCoordinates
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessBoundingBox
import com.example.scrollbooker.entity.booking.business.data.remote.SearchBusinessRequest
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMarker
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessOwner
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfile
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessSheet
import com.example.scrollbooker.entity.booking.business.domain.useCase.GetBusinessProfileUseCase
import com.example.scrollbooker.entity.booking.business.domain.useCase.GetBusinessesMarkersUseCase
import com.example.scrollbooker.entity.booking.business.domain.useCase.GetBusinessesSheetUseCase
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.useCase.GetAllBusinessDomainsUseCase
import com.example.scrollbooker.entity.nomenclature.businessType.domain.model.BusinessType
import com.example.scrollbooker.entity.nomenclature.businessType.domain.useCase.GetAllBusinessTypesByBusinessDomainUseCase
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.Filter
import com.example.scrollbooker.entity.nomenclature.filter.domain.useCase.GetFiltersByServiceUseCase
import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service
import com.example.scrollbooker.entity.nomenclature.service.domain.useCase.GetServicesByBusinessTypeUseCase
import com.example.scrollbooker.entity.nomenclature.service.domain.useCase.GetServicesByServiceDomainUseCase
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.useCase.GetAllServiceDomainsByBusinessDomainUseCase
import com.example.scrollbooker.ui.search.sheets.filters.SearchFiltersSheetState
import com.example.scrollbooker.ui.search.sheets.services.SearchServicesFiltersSheetState
import com.mapbox.geojson.Feature
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import toIsoString
import java.math.BigDecimal
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
    val serviceDomainId: Int? = null,
    val serviceId: Int? = null,
    val subFilterIds: Set<Int> = emptySet(),
    val maxPrice: BigDecimal? = BigDecimal(1500),
    val sort: SearchSortEnum = SearchSortEnum.RECOMMENDED,
    val isLastMinute: Boolean = false,
    val hasDiscount: Boolean = false,
    val hasVideo: Boolean = false,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null
)

data class SearchRequestState(
    val bBox: BusinessBoundingBox? = null,
    val zoom: Float = 12f,
    val userLocation: BusinessCoordinates? = null,
    val filters: SearchFiltersState = SearchFiltersState()
) {
    fun toRequest(): SearchBusinessRequest? {
        val box = bBox ?: return null

        return SearchBusinessRequest(
            bbox = box,
            zoom = zoom,
            maxMarkers = 400,
            businessDomainId = filters.businessDomainId,
            serviceDomainId = filters.serviceDomainId,
            serviceId = filters.serviceId,
            subFilterIds = filters.subFilterIds.toList(),
            userLocation = userLocation,
            maxPrice = filters.maxPrice,
            sort = filters.sort.raw,
            hasDiscount = filters.hasDiscount,
            isLastMinute = filters.isLastMinute,
            hasVideo = filters.hasVideo,
            startDate = filters.startDate?.toIsoString(),
            endDate = filters.endDate?.toIsoString(),
            startTime = filters.startTime?.toPrettyTime(),
            endTime = filters.endTime?.toPrettyTime()
        )
    }
}

fun SearchRequestState.activeFiltersCount(): Int {
    return listOf(
        filters.hasVideo,
        filters.hasDiscount,
        filters.isLastMinute,
        filters.maxPrice != BigDecimal(1500),
        filters.sort != SearchSortEnum.RECOMMENDED
    ).count { it }
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getBusinessesMarkersUseCase: GetBusinessesMarkersUseCase,
    private val getBusinessesSheetUseCase: GetBusinessesSheetUseCase,
    private val getAllBusinessDomainsUseCase: GetAllBusinessDomainsUseCase,
    private val getAllBusinessTypesByBusinessDomainUseCase: GetAllBusinessTypesByBusinessDomainUseCase,
    private val getAllServiceDomainsByBusinessDomainUseCase: GetAllServiceDomainsByBusinessDomainUseCase,
    private val getServicesByServiceDomainUseCase: GetServicesByServiceDomainUseCase,
    private val getFiltersByServiceUseCase: GetFiltersByServiceUseCase,
    private val getBusinessProfileUseCase: GetBusinessProfileUseCase
): ViewModel() {
    private val _request = MutableStateFlow(SearchRequestState())
    val request: StateFlow<SearchRequestState> = _request.asStateFlow()

    private val _businessDomains =
        MutableStateFlow<FeatureState<List<BusinessDomain>>>(FeatureState.Loading)
    val businessDomains: StateFlow<FeatureState<List<BusinessDomain>>> =
        _businessDomains.asStateFlow()

    private val _markersUiState = MutableStateFlow(MarkersUiState())
    val markersUiState: StateFlow<MarkersUiState> = _markersUiState.asStateFlow()

    private val _selectedBusinessOwner = MutableStateFlow<BusinessOwner?>(null)
    val selectedBusinessOwner: StateFlow<BusinessOwner?> = _selectedBusinessOwner.asStateFlow()

    private val _selectedMarker = MutableStateFlow<BusinessMarker?>(null)
    val selectedMarker: StateFlow<BusinessMarker?> = _selectedMarker.asStateFlow()

    private val _sheetUiState = MutableStateFlow(SheetUiState())

    private val _sheetTotalCount = MutableStateFlow(0)
    val sheetTotalCount: StateFlow<Int> = _sheetTotalCount.asStateFlow()

    private val _isMapReady = MutableStateFlow<Boolean>(false)
    val isMapReady: StateFlow<Boolean> = _isMapReady.asStateFlow()

    private val _isStyleLoaded = MutableStateFlow<Boolean>(false)
    val isStyleLoaded: StateFlow<Boolean> = _isStyleLoaded.asStateFlow()

    private val _cameraPosition = MutableStateFlow<CameraPositionState>(CameraPositionState())
    val cameraPosition: StateFlow<CameraPositionState> = _cameraPosition.asStateFlow()

    private val _servicesSheetFilters = MutableStateFlow(SearchServicesFiltersSheetState())
    val servicesSheetFilters: StateFlow<SearchServicesFiltersSheetState> =
        _servicesSheetFilters.asStateFlow()

    private val rawRequestFlow: Flow<SearchBusinessRequest> =
        _request
            .mapNotNull { it.toRequest() }
            .debounce(100L)
            .distinctUntilChanged()

    private val sharedRequestFlow = rawRequestFlow
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000)
        )

    init {
        sharedRequestFlow
            .flatMapLatest { req ->
                flow {
                    _markersUiState.update { current ->
                        current.copy(isLoading = true, error = null)
                    }

                    val result = withVisibleLoading { getBusinessesMarkersUseCase(req) }

                    when (result) {
                        is FeatureState.Success -> _markersUiState.update { current ->
                            current.copy(
                                data = result.data,
                                isLoading = false,
                                error = null
                            )
                        }

                        is FeatureState.Error -> _markersUiState.update { current ->
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
                getBusinessesSheetUseCase(
                    request = req,
                    onTotalCountChanged = { total -> _sheetTotalCount.value = total },
                )
            }
            .cachedIn(viewModelScope)

    val serviceDomains: StateFlow<FeatureState<List<ServiceDomain>>?> =
        _servicesSheetFilters
            .map { it.businessDomainId }
            .filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest { domainId ->
                flow {
                    if (domainId == null) {
                        emit(null)
                    } else {
                        emit(FeatureState.Loading)

                        val result = withVisibleLoading {
                            getAllServiceDomainsByBusinessDomainUseCase(domainId)
                        }

                        emit(result)
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    val services: StateFlow<FeatureState<List<Service>>?> =
        _servicesSheetFilters
            .map { it.serviceDomainId }
            .filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest { typeId ->
                flow {
                    if (typeId == null) {
                        emit(null)
                    } else {
                        emit(FeatureState.Loading)

                        val result = withVisibleLoading {
                            getServicesByServiceDomainUseCase(typeId)
                        }

                        val featureState: FeatureState<List<Service>> =
                            result.fold(
                                onSuccess = { s -> FeatureState.Success(s) },
                                onFailure = { e -> FeatureState.Error(e) }
                            )

                        emit(featureState)
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    val filters: StateFlow<FeatureState<List<Filter>>?> =
        _servicesSheetFilters
            .map { it.serviceId }
            .distinctUntilChanged()
            .flatMapLatest { serviceId ->
                flow {
                    if (serviceId == null) {
                        emit(null)
                    } else {
                        emit(FeatureState.Loading)

                        val result = withVisibleLoading {
                            getFiltersByServiceUseCase(serviceId)
                        }

                        emit(result)
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    init {
        loadAllBusinessDomains()
    }

    fun setSelectedMarker(marker: BusinessMarker?) {
        _selectedMarker.value = marker
    }

    fun setBusinessOwner(businessOwner: BusinessOwner) {
        _selectedBusinessOwner.value = businessOwner
    }

    fun clearBusinessOwner() {
        _selectedBusinessOwner.value = null
    }

    fun loadAllBusinessDomains() {
        viewModelScope.launch {
            _businessDomains.value = FeatureState.Loading
            _businessDomains.value = withVisibleLoading { getAllBusinessDomainsUseCase() }
        }
    }

    fun updateCamera(position: CameraPositionState) {
        _cameraPosition.value = position
    }

    fun onMapIdle(bBox: BusinessBoundingBox) {
        val zoom = _cameraPosition.value.zoom.toFloat()

        _request.update { current ->
            if (current.bBox == bBox && current.zoom == zoom) {
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
                    businessDomainId = domainId,
                    serviceDomainId = null,
                    serviceId = null,
                    subFilterIds = emptySet()
                )
            )
        }
        _servicesSheetFilters.update { current ->
            current.copy(
                businessDomainId = domainId,
                serviceDomainId = null,
                serviceId = null,
                subFilterIds = emptySet()
            )
        }
    }

    // Filters Sheet Filters
    fun setFiltersFromFiltersSheet(filtersSheet: SearchFiltersSheetState) {
        _request.update { current ->
            current.copy(
                filters = current.filters.copy(
                    maxPrice = filtersSheet.maxPrice,
                    sort = filtersSheet.sort,
                    hasDiscount = filtersSheet.hasDiscount,
                    isLastMinute = filtersSheet.isLastMinute,
                    hasVideo = filtersSheet.hasVideo
                )
            )
        }
    }

    fun clearFiltersFromFiltersSheet() {
        _request.update { current ->
            current.copy(
                filters = current.filters.copy(
                    maxPrice = BigDecimal(1500),
                    sort = SearchSortEnum.RECOMMENDED,
                    hasDiscount = false,
                    isLastMinute = false,
                    hasVideo = false
                )
            )
        }
    }

    // Services Sheet Filters
    fun setFiltersFromServicesSheet(sheet: SearchServicesFiltersSheetState) {
        _request.update { current ->
            current.copy(
                filters = current.filters.copy(
                    businessDomainId = sheet.businessDomainId,
                    serviceDomainId = sheet.serviceDomainId,
                    serviceId = sheet.serviceId,
                    subFilterIds = sheet.subFilterIds,
                    startDate = sheet.startDate,
                    endDate = sheet.endDate,
                    startTime = sheet.startTime,
                    endTime = sheet.endTime
                )
            )
        }
    }

    fun clearServicesFiltersSheet() {
        _servicesSheetFilters.update {
            it.copy(
                businessDomainId = null,
                serviceDomainId = null,
                serviceId = null,
                subFilterIds = emptySet(),
                startDate = null,
                endDate = null,
                startTime = null,
                endTime = null
            )
        }
    }

    fun onSheetBusinessDomainSelected(domainId: Int?) {
        _servicesSheetFilters.update {
            it.copy(
                businessDomainId = domainId,
                serviceDomainId = null,
                serviceId = null,
                subFilterIds = emptySet()
            )
        }
    }

    fun setServiceDomainId(typeId: Int?) {
        _servicesSheetFilters.update {
            it.copy(
                serviceDomainId = typeId,
                serviceId = null,
                subFilterIds = emptySet()
            )
        }
    }

    fun setServiceId(serviceId: Int?) {
        _servicesSheetFilters.update {
            it.copy(
                serviceId = serviceId,
                subFilterIds = emptySet()
            )
        }
    }

    fun setSubFilterId(subFilterId: Int) {
        _servicesSheetFilters.update { current ->
            val currentIds = current.subFilterIds
            val newIds = currentIds + subFilterId

            current.copy(subFilterIds = newIds)
        }
    }

    fun setDateTime(
        startDate: LocalDate?,
        endDate: LocalDate?,
        startTime: LocalTime?,
        endTime: LocalTime?
    ) {
        _servicesSheetFilters.update { current ->
            current.copy(
                startDate = startDate,
                endDate = endDate,
                startTime = startTime,
                endTime = endTime
            )
        }
    }
}
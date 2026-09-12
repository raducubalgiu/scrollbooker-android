package com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient

import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.components.customized.calendar.BaseCalendarViewModel
import com.example.scrollbooker.components.customized.calendar.CalendarContext
import com.example.scrollbooker.core.snackbar.SnackBarUiEvent
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.availability.domain.model.AvailableDay
import com.example.scrollbooker.entity.booking.availability.domain.useCase.GetCalendarAvailableDaysUseCase
import com.example.scrollbooker.entity.booking.availability.domain.useCase.GetUserAvailableTimeslotsUseCase
import com.example.scrollbooker.entity.booking.businessClient.domain.model.BusinessClient
import com.example.scrollbooker.entity.booking.businessClient.domain.model.BusinessClientCreate
import com.example.scrollbooker.entity.booking.businessClient.domain.useCase.CreateBusinessClientUseCase
import com.example.scrollbooker.entity.booking.businessClient.domain.useCase.SearchBusinessClientsUseCase
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByBusinessIdAndEmployeeIdUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class AddOwnClientViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val searchBusinessClientsUseCase: SearchBusinessClientsUseCase,
    private val createBusinessClientUseCase: CreateBusinessClientUseCase,
    private val getProductsByBusinessIdAndEmployeeIdUseCase: GetProductsByBusinessIdAndEmployeeIdUseCase,
    getCalendarAvailableDaysUseCase: GetCalendarAvailableDaysUseCase,
    private val getUserAvailableTimeslotsUseCase: GetUserAvailableTimeslotsUseCase,
): BaseCalendarViewModel(getCalendarAvailableDaysUseCase) {
    private val userIdFlow: Flow<Int?> = authDataStore.getUserId().distinctUntilChanged()
    private val businessIdFlow: Flow<Int?> = authDataStore.getBusinessId().distinctUntilChanged()
    private val businessOwnerIdFlow: Flow<Int?> = authDataStore.getBusinessOwnerId().distinctUntilChanged()

    private val employeeIdFlow: Flow<Int?> = combine(
        businessOwnerIdFlow,
        userIdFlow
    ) { businessOwnerId, userId ->
        if (businessOwnerId != null && userId != null && businessOwnerId != userId) userId else null
    }.distinctUntilChanged()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _searchState = MutableStateFlow<FeatureState<List<BusinessClient>>?>(null)
    val searchState: StateFlow<FeatureState<List<BusinessClient>>?> = _searchState.asStateFlow()

    private val _selectedClient = MutableStateFlow<BusinessClient?>(null)
    val selectedClient: StateFlow<BusinessClient?> = _selectedClient.asStateFlow()

    private val _isCreating = MutableStateFlow(false)
    val isCreating: StateFlow<Boolean> = _isCreating.asStateFlow()

    private val _userProducts = MutableStateFlow<FeatureState<UserProducts>>(FeatureState.Loading)
    val userProducts: StateFlow<FeatureState<UserProducts>> = _userProducts.asStateFlow()

    private val _linkedProducts = MutableStateFlow<Set<Product>>(emptySet())
    val linkedProducts: StateFlow<Set<Product>> = _linkedProducts.asStateFlow()

    private val _events = MutableSharedFlow<SnackBarUiEvent.Show>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<SnackBarUiEvent.Show> = _events.asSharedFlow()

    // No fallback duration - the date/time picker is disabled by the screen until at least one
    // service is selected, so this only ever drives a real fetch once linkedProducts is non-empty.
    private val slotDurationFlow: StateFlow<Int> = _linkedProducts
        .map { products -> products.sumOf { it.startingOffering.duration } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    override val calendarContextFlow: Flow<CalendarContext> = combine(
        userIdFlow.filterNotNull(),
        businessIdFlow.filterNotNull(),
        employeeIdFlow,
        slotDurationFlow
    ) { userId, businessId, employeeId, duration ->
        CalendarContext(userId, businessId, employeeId, duration)
    }.distinctUntilChanged()

    override fun calendarWindow(currentMonday: LocalDate): Pair<LocalDate, LocalDate> =
        currentMonday to currentMonday.plusWeeks(26)

    private val _selectedCalendarDay = MutableStateFlow<LocalDate?>(null)
    val selectedCalendarDay: StateFlow<LocalDate?> = _selectedCalendarDay.asStateFlow()

    private val daySlotsCache = ConcurrentHashMap<Triple<LocalDate, Int, Int?>, AvailableDay>()

    val daySlots: StateFlow<FeatureState<AvailableDay>?> = combine(
        _selectedCalendarDay,
        slotDurationFlow,
        employeeIdFlow
    ) { day, duration, employeeId -> Triple(day, duration, employeeId) }
        .flatMapLatest { (day, duration, employeeId) ->
            if (day == null) {
                flow { emit(null) }
            } else {
                flow {
                    val cacheKey = Triple(day, duration, employeeId)
                    val cached = daySlotsCache[cacheKey]

                    if (cached != null) {
                        emit(FeatureState.Success(cached))
                        return@flow
                    }

                    emit(FeatureState.Loading)

                    val businessId = businessIdFlow.first()
                    if (businessId == null) {
                        emit(FeatureState.Error(IllegalStateException("Missing businessId")))
                        return@flow
                    }

                    val result = withVisibleLoading {
                        getUserAvailableTimeslotsUseCase(
                            businessId = businessId,
                            employeeId = employeeId,
                            slotDuration = duration,
                            day = day.toString()
                        )
                    }

                    emit(
                        result.fold(
                            onSuccess = { availableDay ->
                                daySlotsCache[cacheKey] = availableDay
                                FeatureState.Success(availableDay)
                            },
                            onFailure = { e ->
                                Timber.tag("AddOwnClient").e("ERROR: on fetching day slots $e")
                                FeatureState.Error(e)
                            }
                        )
                    )
                }
            }
        }
        .catch { e -> emit(FeatureState.Error(e)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun selectCalendarDay(day: LocalDate) {
        _selectedCalendarDay.value = day
    }

    init {
        _query
            .map { it.trim() }
            .distinctUntilChanged()
            .debounce(200)
            .onEach { query ->
                if (query.length < 2) {
                    _searchState.value = null
                    return@onEach
                }
                _searchState.value = FeatureState.Loading
            }
            .filter { it.length >= 2 }
            .mapLatest { query ->
                val businessId = businessIdFlow.first()

                if (businessId == null) {
                    FeatureState.Error(IllegalStateException("Missing businessId"))
                } else {
                    val result = withVisibleLoading { searchBusinessClientsUseCase(businessId, query) }
                    result.fold(
                        onSuccess = { FeatureState.Success(it) },
                        onFailure = { e ->
                            Timber.tag("BusinessClients").e("ERROR: on searching business clients $e")
                            FeatureState.Error(e)
                        }
                    )
                }
            }
            .onEach { result -> _searchState.value = result }
            .catch { e -> _searchState.value = FeatureState.Error(e) }
            .launchIn(viewModelScope)
    }

    fun handleSearch(query: String) {
        _query.value = query
    }

    fun selectClient(client: BusinessClient?) {
        _selectedClient.value = client
    }

    fun createClient(fullname: String, phone: String?) {
        viewModelScope.launch {
            _isCreating.value = true

            val businessId = businessIdFlow.first()
            if (businessId == null) {
                _isCreating.value = false
                return@launch
            }

            val result = withVisibleLoading {
                createBusinessClientUseCase(businessId, BusinessClientCreate(fullname, phone))
            }

            result
                .onFailure { e ->
                    Timber.tag("BusinessClients").e("ERROR: on creating business client $e")
                    _events.tryEmit(SnackBarUiEvent.somethingWentWrong())
                }
                .onSuccess { client ->
                    _selectedClient.value = client
                }

            _isCreating.value = false
        }
    }

    fun loadUserProducts() {
        viewModelScope.launch {
            _userProducts.value = FeatureState.Loading

            val businessId = businessIdFlow.first()
            if (businessId == null) {
                _userProducts.value = FeatureState.Error(IllegalStateException("Missing businessId"))
                return@launch
            }

            val employeeId = employeeIdFlow.first()

            _userProducts.value = getProductsByBusinessIdAndEmployeeIdUseCase(
                businessId = businessId,
                employeeId = employeeId,
                onlyServicesWithProducts = true,
                productsLimitPerService = null
            )
        }
    }

    fun setLinkedProducts(products: Set<Product>) {
        _linkedProducts.value = products
    }

    fun removeLinkedProduct(product: Product) {
        _linkedProducts.value = _linkedProducts.value - product
    }
}

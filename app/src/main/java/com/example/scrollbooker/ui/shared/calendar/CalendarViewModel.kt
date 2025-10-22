package com.example.scrollbooker.ui.shared.calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentScrollBookerCreate
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.CreateScrollBookerAppointmentUseCase
import com.example.scrollbooker.entity.booking.calendar.domain.model.AvailableDay
import com.example.scrollbooker.entity.booking.calendar.domain.model.Slot
import com.example.scrollbooker.entity.booking.calendar.domain.useCase.GetCalendarAvailableDaysUseCase
import com.example.scrollbooker.entity.booking.calendar.domain.useCase.GetUserAvailableTimeslotsUseCase
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getCalendarAvailableDaysUseCase: GetCalendarAvailableDaysUseCase,
    private val getUserAvailableTimeslotsUseCase: GetUserAvailableTimeslotsUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val createScrollBookerAppointmentUseCase: CreateScrollBookerAppointmentUseCase,
): ViewModel() {
    private val userId = MutableStateFlow<Int?>(null)
    private val selectedDay = MutableStateFlow<LocalDate?>(LocalDate.now())
    private val slotDuration = MutableStateFlow<Int?>(null)

    private val _product = MutableStateFlow<FeatureState<Product>>(FeatureState.Loading)
    val product: StateFlow<FeatureState<Product>> = _product.asStateFlow()

    private val _selectedSlot = MutableStateFlow<Slot?>(null)
    val selectedSlot: StateFlow<Slot?> = _selectedSlot.asStateFlow()

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _forceRefresh = MutableStateFlow<Boolean>(false)
    val forceRefresh: StateFlow<Boolean> = _forceRefresh.asStateFlow()

    private val cachedSlots = mutableMapOf<LocalDate, FeatureState<AvailableDay>>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val calendarHeader: StateFlow<FeatureState<CalendarHeaderState>> = userId
        .filterNotNull()
        .flatMapLatest { userId ->
            getCalendarHeader(userId)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, FeatureState.Loading)

    private fun getCalendarHeader(userId: Int): StateFlow<FeatureState<CalendarHeaderState>> = flow {
        emit(FeatureState.Loading)

        try {
            val today = LocalDate.now()
            val startDate = today.with(DayOfWeek.MONDAY)
            val endDate = startDate.plusWeeks(13)
            val calendarDays = (0 until 13 * 7).map { startDate.plusDays(it.toLong()) }

            val availableDays = withVisibleLoading {
                getCalendarAvailableDaysUseCase(userId, startDate.toString(), endDate.toString())
            }

            emit(
                FeatureState.Success(
                    CalendarHeaderState(
                        config = CalendarConfig(
                            userId = userId,
                            startDate = startDate,
                            endDate = endDate,
                            totalWeeks = 13,
                            initialWeekPage = 0,
                            initialDayPage = today.dayOfWeek.ordinal,
                            selectedDay = today
                        ),
                        calendarDays = calendarDays,
                        calendarAvailableDays = availableDays.map {LocalDate.parse(it) },
                    )
                )
            )
        } catch (e: Exception) {
            Timber.tag("Calendar").e("ERROR: on Fetching Calendar Header: $e")
            emit(FeatureState.Error(e))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = FeatureState.Loading
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val availableDayState: StateFlow<FeatureState<AvailableDay>> = combine(
        selectedDay.filterNotNull(),
        userId.filterNotNull(),
        slotDuration.filterNotNull(),
        forceRefresh,
    ) {
        day, userId, slotDuration, forceRefresh ->
        SlotsParams(day, userId, slotDuration, forceRefresh)
    }
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { params ->
            loadSlotsForDay(params)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            FeatureState.Loading
        )

    fun loadSlotsForDay(params: SlotsParams): Flow<FeatureState<AvailableDay>> = flow {
        with(params) {
            if(!forceRefresh && cachedSlots.containsKey(day)) {
                emit(cachedSlots[day]!!)
                return@flow
            }

            emit(FeatureState.Loading)

            val response = withVisibleLoading {
                getUserAvailableTimeslotsUseCase(
                    day = day.toString(),
                    userId = userId,
                    slotDuration = slotDuration
                )
            }

            cachedSlots[day] = response
            emit(response)
        }
    }

    fun setUserId(id: Int) {
        userId.value = id
    }

    fun setDay(day: LocalDate) {
        selectedDay.value = day
    }

    fun setSlotDuration(duration: Int) {
        slotDuration.value = duration
    }

    fun setSelectedSlot(slot: Slot) {
        _selectedSlot.value = slot
    }

    fun reset() {
        userId.value = null
        selectedDay.value = null
        slotDuration.value = null
        _selectedSlot.value = null
    }

    fun handleRefresh() {
        _forceRefresh.value = true

        viewModelScope.launch {
            availableDayState
                .drop(1)
                .collectLatest {
                    if(it !is FeatureState.Loading) {
                        _forceRefresh.value = false
                        cancel()
                    }
                }
        }
    }

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            _product.value = FeatureState.Loading

            val response = withVisibleLoading {
                getProductByIdUseCase(productId)
            }

            _product.value = response
        }
    }

    suspend fun createAppointment(): Result<Unit> {
        _isSaving.value = true

        val selectedProduct = _product.value
        val startDate = _selectedSlot.value?.startDateUtc
        val endDate = _selectedSlot.value?.endDateUtc

        val isProductSuccess = selectedProduct is FeatureState.Success

        if(startDate.isNullOrBlank() || endDate.isNullOrBlank() || !isProductSuccess) {
            Timber.tag("Create Appointment").e("ERROR: on Creating ScrollBooker Appointment, the provided data are invalid")
            return Result.failure(Exception("Invalid data"))
        }

        val appointment = AppointmentScrollBookerCreate(
            startDate = startDate,
            endDate = endDate,
            userId = selectedProduct.data.userId,
            currencyId = selectedProduct.data.currencyId,
            serviceId = selectedProduct.data.serviceId,
            productId = selectedProduct.data.id,
        )

        val result = withVisibleLoading {
            createScrollBookerAppointmentUseCase(appointment)
        }

        result
            .onFailure { e ->
                _isSaving.value = false
                Timber.tag("Appointment").e("ERROR: onCreating ScrollBooker Appointment $e")
            }
            .onSuccess {
                _isSaving.value = false
            }

        return result
    }
}
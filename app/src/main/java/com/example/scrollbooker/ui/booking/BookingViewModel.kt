package com.example.scrollbooker.ui.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentScrollBookerCreateDto
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.CreateScrollBookerAppointmentUseCase
import com.example.scrollbooker.entity.booking.availability.domain.model.AvailableDay
import com.example.scrollbooker.entity.booking.availability.domain.model.Slot
import com.example.scrollbooker.entity.booking.availability.domain.useCase.GetCalendarAvailableDaysUseCase
import com.example.scrollbooker.entity.booking.availability.domain.useCase.GetUserAvailableTimeslotsUseCase
import com.example.scrollbooker.entity.booking.booking.domain.model.BookingFlow
import com.example.scrollbooker.entity.booking.booking.domain.useCase.GetBookingFlowUseCase
import com.example.scrollbooker.ui.shared.calendar.CalendarConfig
import com.example.scrollbooker.ui.shared.calendar.CalendarHeaderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBookingFlowUseCase: GetBookingFlowUseCase,
    private val getCalendarAvailableDaysUseCase: GetCalendarAvailableDaysUseCase,
    private val getUserAvailableTimeslotsUseCase: GetUserAvailableTimeslotsUseCase,
    private val createScrollBookerAppointmentUseCase: CreateScrollBookerAppointmentUseCase,
): ViewModel() {
    val businessId: Int = checkNotNull(savedStateHandle["businessId"]) {
        "businessId mandatory parameter is missing in Booking flow"
    }
    val businessOwnerId: Int = checkNotNull(savedStateHandle["businessOwnerId"]) {
        "businessOwnerId mandatory parameter is missing in Booking flow"
    }
    val userId: Int = checkNotNull(savedStateHandle["userId"]) {
        "userId mandatory parameter is missing in Booking flow"
    }
    val source: String = checkNotNull(savedStateHandle["source"]) {
        "source mandatory parameter is missing in Booking flow"
    }
    val initialSelectedProductId: Int = savedStateHandle["selectedProductId"] ?: -1

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    val isEmployee: Boolean = businessOwnerId != userId

    private val _selectedEmployeeId = MutableStateFlow<Int?>(
        if (businessOwnerId != userId) userId else null
    )
    val selectedEmployeeId: StateFlow<Int?> = _selectedEmployeeId.asStateFlow()

    fun setSelectedEmployeeId(employeeId: Int) {
        _selectedEmployeeId.value = employeeId
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val bookingFlowState: StateFlow<FeatureState<BookingFlow>> = _selectedEmployeeId
        .flatMapLatest { currentEmployeeId ->
            flow {
                val result = withVisibleLoading {
                    getBookingFlowUseCase(
                        businessId = businessId,
                        employeeId = currentEmployeeId,
                    )
                }
                emit(result)
            }
        }
        .catch { e ->
            emit(FeatureState.Error(e as? Exception ?: Exception(e)))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FeatureState.Loading
        )

    private val _isInitialProductProcessed = MutableStateFlow(false)
    val isInitialProductProcessed = _isInitialProductProcessed.asStateFlow()

    fun markInitialProductAsProcessed() {
        _isInitialProductProcessed.value = true
    }

    private val _selectedBookingItems = MutableStateFlow<List<SelectedBookingItem>>(emptyList())
    val selectedBookingItems: StateFlow<List<SelectedBookingItem>> =
        _selectedBookingItems.asStateFlow()

    val bookingTotals: StateFlow<BookingTotals> = combine(
        selectedBookingItems,
        selectedEmployeeId
    ) { items, employeeId ->
        val sumPrice = items.sumOf { item ->
            if (employeeId != null) {
                val specificOffering = item.offerings.find { it.user.id == employeeId }
                specificOffering?.priceWithDiscount ?: BigDecimal.ZERO
            } else {
                item.offerings.firstOrNull()?.priceWithDiscount ?: BigDecimal.ZERO
            }
        }
        
        val sumDuration = items.sumOf { item -> item.variantDuration }

        BookingTotals(
            totalPrice = sumPrice,
            totalDuration = sumDuration
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BookingTotals(BigDecimal.ZERO, 0)
        )

    fun selectBookingItem(item: SelectedBookingItem) {
        val currentItems = _selectedBookingItems.value.toMutableList()
        val existingItemWithSameProduct = currentItems.find { it.productId == item.productId }

        if (existingItemWithSameProduct != null) {
            if (existingItemWithSameProduct.variantId == item.variantId) {
                currentItems.remove(existingItemWithSameProduct)
            } else {
                val index = currentItems.indexOf(existingItemWithSameProduct)
                if (index != -1) {
                    currentItems[index] = item
                }
            }
        } else {
            currentItems.add(item)
        }

        _selectedBookingItems.value = currentItems
    }

    private val _selectedDay = MutableStateFlow<LocalDate>(LocalDate.now())
    val selectedDay: StateFlow<LocalDate> = _selectedDay.asStateFlow()

    private val _selectedSlot = MutableStateFlow<Slot?>(null)
    val selectedSlot: StateFlow<Slot?> = _selectedSlot.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val calendarHeader: StateFlow<FeatureState<CalendarHeaderState>> = _selectedEmployeeId
        .flatMapLatest { currentEmployeeId ->
            flow {
                emit(FeatureState.Loading)

                val today = LocalDate.now()
                val currentMonday = today.with(DayOfWeek.MONDAY)
                val startDate = currentMonday
                val endDate = currentMonday.plusWeeks(26)
                val totalWeeks = 26

                val calendarDays = (0 until (totalWeeks * 7)).map {
                    startDate.plusDays(it.toLong())
                }

                val availableDays = withVisibleLoading {
                    getCalendarAvailableDaysUseCase(
                        businessId = businessId,
                        employeeId = currentEmployeeId,
                        startDate = startDate.toString(),
                        endDate = endDate.toString()
                    )
                }

                emit(
                    FeatureState.Success(
                        CalendarHeaderState(
                            config = CalendarConfig(
                                userId = userId,
                                startDate = startDate,
                                endDate = endDate,
                                totalWeeks = totalWeeks,
                                initialWeekPage = 0,
                                initialDayPage = today.dayOfWeek.ordinal,
                                selectedDay = today
                            ),
                            calendarDays = calendarDays,
                            calendarAvailableDays = availableDays.map { LocalDate.parse(it) },
                        )
                    )
                )
            }
                .flowOn(Dispatchers.Default)
                .catch { emit(FeatureState.Error(it)) }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, FeatureState.Loading)

    private val slotDuration: StateFlow<Int> = _selectedBookingItems
        .map { items ->
            val total = items.sumOf { it.variantDuration }
            if (total > 0) total else 0
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 0
        )

    private val slotsCache = mutableMapOf<Triple<LocalDate, Int, Int?>, AvailableDay>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val availableSlots: StateFlow<FeatureState<AvailableDay>> = combine(
        selectedDay,
        slotDuration,
        selectedEmployeeId
    ) { day, duration, empId ->
        Triple(day, duration, empId)
    }
        .flatMapLatest { (day, duration, empId) ->
            flow {
                val cacheKey = Triple(day, duration, empId)

                if (slotsCache.containsKey(cacheKey)) {
                    emit(FeatureState.Success(slotsCache[cacheKey]!!))
                } else {
                    emit(FeatureState.Loading)

                    val availableDayData = withVisibleLoading {
                        getUserAvailableTimeslotsUseCase(
                            businessId = businessId,
                            employeeId = empId,
                            slotDuration = duration,
                            day = day.toString(),
                        )
                    }

                    slotsCache[cacheKey] = availableDayData
                    emit(FeatureState.Success(availableDayData))
                }
            }
                .flowOn(Dispatchers.IO)
                .catch { exception ->
                    Timber.tag("Calendar").e("ERROR: on Fetching User Available Timeslots $exception")
                    emit(FeatureState.Error(exception))
                }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            FeatureState.Loading
        )

    suspend fun createAppointment(): Result<Unit> {
        _isSaving.value = true

        val startDate = _selectedSlot.value?.startDateUtc
        val endDate = _selectedSlot.value?.endDateUtc

        if(startDate.isNullOrBlank() || endDate.isNullOrBlank()) {
            Timber.tag("Create Appointment").e("ERROR: on Creating ScrollBooker Appointment, the provided data are invalid")
            return Result.failure(Exception("Invalid data"))
        }

        val appointment = AppointmentScrollBookerCreateDto(
            startDate = startDate,
            endDate = endDate,
            productVariants = _selectedBookingItems.value.toProductVariantsDto(),
            paymentCurrencyId = 1,
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

    fun clearSlotsCache() {
        slotsCache.clear()
    }

    fun onDaySelected(date: LocalDate) {
        _selectedDay.value = date
    }

    fun onSlotSelected(slot: Slot) {
        _selectedSlot.value = slot
    }
}
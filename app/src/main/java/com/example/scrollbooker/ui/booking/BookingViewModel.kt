package com.example.scrollbooker.ui.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.snackbar.SnackBarType
import com.example.scrollbooker.core.snackbar.SnackBarUiEvent
import com.example.scrollbooker.core.snackbar.UiText
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentScrollBookerCreateDto
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.CreateScrollBookerAppointmentUseCase
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.GetAppointmentByIdUseCase
import com.example.scrollbooker.entity.booking.availability.domain.model.AvailableDay
import com.example.scrollbooker.entity.booking.availability.domain.model.Slot
import com.example.scrollbooker.entity.booking.availability.domain.useCase.GetCalendarAvailableDaysUseCase
import com.example.scrollbooker.entity.booking.availability.domain.useCase.GetUserAvailableTimeslotsUseCase
import com.example.scrollbooker.entity.booking.booking.domain.model.BookingFlow
import com.example.scrollbooker.entity.booking.booking.domain.useCase.GetBookingFlowUseCase
import com.example.scrollbooker.entity.booking.products.domain.model.toBookingItem
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.calendar.BaseCalendarViewModel
import com.example.scrollbooker.components.customized.calendar.CalendarContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.threeten.bp.LocalDate
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBookingFlowUseCase: GetBookingFlowUseCase,
    getCalendarAvailableDaysUseCase: GetCalendarAvailableDaysUseCase,
    private val getUserAvailableTimeslotsUseCase: GetUserAvailableTimeslotsUseCase,
    private val createScrollBookerAppointmentUseCase: CreateScrollBookerAppointmentUseCase,
    private val getAppointmentByIdUseCase: GetAppointmentByIdUseCase,
): BaseCalendarViewModel(getCalendarAvailableDaysUseCase) {
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
    val postId: Int = savedStateHandle["postId"] ?: -1
    val appointmentId: Int = savedStateHandle["appointmentId"] ?: -1

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    val isEmployee: Boolean = businessOwnerId != userId

    private val _selectedEmployeeId = MutableStateFlow<Int?>(
        if (businessOwnerId != userId) userId else null
    )
    val selectedEmployeeId: StateFlow<Int?> = _selectedEmployeeId.asStateFlow()

    private val _events = MutableSharedFlow<SnackBarUiEvent.Show>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events = _events.asSharedFlow()

    fun setSelectedEmployeeId(employeeId: Int) {
        _selectedEmployeeId.value = employeeId
    }

    val bookingFlowState: StateFlow<FeatureState<BookingFlow>> = flow {
        emit(FeatureState.Loading)

        val result = withVisibleLoading {
            getBookingFlowUseCase(
                businessId = businessId,
                employeeId = _selectedEmployeeId.value,
            )
        }

        val state = result.fold(
            onSuccess = { bookingFlow ->
                FeatureState.Success(bookingFlow)
            },
            onFailure = { throwable ->
                Timber.tag("Booking Flow").e(throwable, "ERROR: on Fetching Booking Flow")
                FeatureState.Error(throwable as? Exception ?: Exception(throwable))
            }
        )

        emit(state)
    }
    .catch { e ->
        Timber.tag("Booking Flow").e(e, "ERROR: Fatal Flow Exception")
        emit(FeatureState.Error(e as? Exception ?: Exception(e)))
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = FeatureState.Loading
    )

    private val _isInitialSelectionProcessed = MutableStateFlow(false)
    val isInitialSelectionProcessed = _isInitialSelectionProcessed.asStateFlow()

    fun markInitialSelectionAsProcessed() {
        _isInitialSelectionProcessed.value = true
    }

    suspend fun processAppointmentRebooking(bookingFlow: BookingFlow): Int? {
        val appointment = runCatching { getAppointmentByIdUseCase(appointmentId) }
            .onFailure { e ->
                Timber.tag("Booking Flow").e(e, "ERROR: on Fetching Appointment for Book Again")
            }
            .getOrNull() ?: return null

        val variantsWithProduct = bookingFlow.products.data
            .flatMap { it.products }
            .flatMap { product -> product.variants.map { variant -> product to variant } }

        var unavailableCount = 0
        var firstMatchedProductId: Int? = null

        appointment.products.forEach { appointmentProduct ->
            val match = appointmentProduct.productVariantId?.let { variantId ->
                variantsWithProduct.find { it.second.id == variantId }
            }
            val isStillOffered = match != null &&
                match.second.offerings.any { it.id == appointmentProduct.offeringId }

            if (match != null && isStillOffered) {
                val (product, variant) = match
                selectBookingItem(variant.toBookingItem(product))

                if (firstMatchedProductId == null) {
                    firstMatchedProductId = product.id
                }
            } else {
                unavailableCount++
            }
        }

        if (unavailableCount > 0) {
            _events.tryEmit(
                SnackBarUiEvent.Show(
                    message = UiText.Resource(R.string.someServicesUnavailable),
                    type = SnackBarType.DEFAULT
                )
            )
        }

        return firstMatchedProductId
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

    fun removeBookingItem(item: SelectedBookingItem) {
        val currentItems = _selectedBookingItems.value.toMutableList()
        currentItems.removeAll { it.productId == item.productId }
        _selectedBookingItems.value = currentItems
    }

    private val _selectedDay = MutableStateFlow<LocalDate>(LocalDate.now())
    val selectedDay: StateFlow<LocalDate> = _selectedDay.asStateFlow()

    private val _selectedSlot = MutableStateFlow<Slot?>(null)
    val selectedSlot: StateFlow<Slot?> = _selectedSlot.asStateFlow()

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

    // Booking only ever looks 6 months into the future - unlike MyCalendarViewModel, which also
    // shows 6 months in the past.
    override val calendarContextFlow: Flow<CalendarContext> = combine(
        _selectedEmployeeId,
        slotDuration
    ) { employeeId, duration ->
        CalendarContext(userId, businessId, employeeId, duration)
    }.distinctUntilChanged()

    override fun calendarWindow(currentMonday: LocalDate): Pair<LocalDate, LocalDate> =
        currentMonday to currentMonday.plusWeeks(26)

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

                    val result = withVisibleLoading {
                        getUserAvailableTimeslotsUseCase(
                            businessId = businessId,
                            employeeId = empId,
                            slotDuration = duration,
                            day = day.toString(),
                        )
                    }

                    val state = result.fold(
                        onSuccess = { availableDayData ->
                            slotsCache[cacheKey] = availableDayData
                            FeatureState.Success(availableDayData)
                        },
                        onFailure = { throwable ->
                            Timber.tag("Calendar").e(throwable, "ERROR: on Fetching User Available Timeslots")
                            FeatureState.Error(throwable as? Exception ?: Exception(throwable))
                        }
                    )

                    emit(state)
                }
            }
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Timber.tag("Calendar").e(e, "Fatal Calendar Flow Exception")
                    emit(FeatureState.Error(e as? Exception ?: Exception(e)))
                }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            FeatureState.Loading
        )

    suspend fun createAppointment(): Result<Appointment> {
        _isSaving.value = true

        val startDate = _selectedSlot.value?.startDateUtc
        val endDate = _selectedSlot.value?.endDateUtc

        if (startDate.isNullOrBlank() || endDate.isNullOrBlank()) {
            Timber.tag("Create Appointment").e("ERROR: on Creating ScrollBooker Appointment, the provided data are invalid")
            _isSaving.value = false
            return Result.failure(Exception("Invalid data"))
        }

        val appointment = AppointmentScrollBookerCreateDto(
            startDate = startDate,
            endDate = endDate,
            productVariants = _selectedBookingItems.value.toProductVariantsDto(),
            paymentCurrencyId = 1,
            postId = if (postId == -1) null else postId
        )

        val result = withVisibleLoading {
            createScrollBookerAppointmentUseCase(appointment)
        }

        result.fold(
            onSuccess = {
                _isSaving.value = false
            },
            onFailure = { e ->
                _isSaving.value = false
                _events.tryEmit(SnackBarUiEvent.somethingWentWrong())
                Timber.tag("Appointment").e(e, "ERROR: on Creating ScrollBooker Appointment")
            }
        )

        return result
    }

    fun onDaySelected(date: LocalDate) {
        _selectedDay.value = date
    }

    fun onSlotSelected(slot: Slot) {
        _selectedSlot.value = slot
    }
}
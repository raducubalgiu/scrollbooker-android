package com.example.scrollbooker.ui.myBusiness.myCalendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentBlockRequest
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentBlockSlots
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentOwnClientCreate
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.CreateBlockAppointmentsUseCase
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.CreateOwnClientAppointmentUseCase
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEvents
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot
import com.example.scrollbooker.entity.booking.calendar.domain.model.blockedStartLocale
import com.example.scrollbooker.entity.booking.calendar.domain.useCase.GetCalendarAvailableDaysUseCase
import com.example.scrollbooker.entity.booking.calendar.domain.useCase.GetUserCalendarEventsUseCase
import com.example.scrollbooker.entity.nomenclature.currency.domain.model.Currency
import com.example.scrollbooker.entity.nomenclature.currency.domain.useCase.GetUserCurrenciesUseCase
import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service
import com.example.scrollbooker.entity.nomenclature.service.domain.useCase.GetServicesByBusinessIdUseCase
import com.example.scrollbooker.store.AuthDataStore
import com.example.scrollbooker.ui.modules.calendar.CalendarConfig
import com.example.scrollbooker.ui.modules.calendar.CalendarHeaderState
import com.mapbox.geojson.Feature
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@HiltViewModel
class MyCalendarViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getCalendarAvailableDaysUseCase: GetCalendarAvailableDaysUseCase,
    private val getCalendarEventsUseCase: GetUserCalendarEventsUseCase,
    private val createBlockAppointmentsUseCase: CreateBlockAppointmentsUseCase,
    private val createOwnClientAppointmentUseCase: CreateOwnClientAppointmentUseCase,
    private val getUserCurrenciesUseCase: GetUserCurrenciesUseCase,
    private val getServicesByBusinessId: GetServicesByBusinessIdUseCase
): ViewModel() {
    private val _selectedDay = MutableStateFlow<LocalDate?>(LocalDate.now())
    val selectedDay: StateFlow<LocalDate?> = _selectedDay.asStateFlow()

    private val _servicesState = MutableStateFlow<FeatureState<List<Service>>>(FeatureState.Loading)
    val servicesState: StateFlow<FeatureState<List<Service>>> = _servicesState

    private val _currenciesState = MutableStateFlow<FeatureState<List<Currency>>>(FeatureState.Loading)
    val currenciesState: StateFlow<FeatureState<List<Currency>>> = _currenciesState

    private val _defaultBlockedStartLocale = MutableStateFlow<Set<LocalDateTime>>(emptySet())
    val defaultBlockedStartLocale: StateFlow<Set<LocalDateTime>> = _defaultBlockedStartLocale.asStateFlow()

    private val _selectedStartLocale = MutableStateFlow<Set<LocalDateTime>>(emptySet())
    val selectedStartLocale: StateFlow<Set<LocalDateTime>> = _selectedStartLocale.asStateFlow()

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val _slotDuration = MutableStateFlow<Int>(30)
    val slotDuration: MutableStateFlow<Int> = _slotDuration

    private val _selectedOwnClient = MutableStateFlow<CalendarEventsSlot?>(null)
    val selectedOwnClient: StateFlow<CalendarEventsSlot?> = _selectedOwnClient.asStateFlow()

    private val refreshTick = MutableStateFlow(0)
    private val userIdFlow: Flow<Int?> = authDataStore.getUserId().distinctUntilChanged()

    private val dateFmt = DateTimeFormatter.ISO_LOCAL_DATE
    private val cache = ConcurrentHashMap<String, FeatureState<CalendarEvents>>()

    private fun cacheKey(userId: Int, day: LocalDate, slot: Int): String =
        "$userId:${day.format(dateFmt)}:$slot"

    private val paramsFlow: Flow<Triple<Int, LocalDate, Int>> =
        combine(
            userIdFlow.filterNotNull(),
            selectedDay.filterNotNull(),
            slotDuration,
            refreshTick
        ) { userId, day, slot, _ ->
            Triple(userId, day, slot)
        }.distinctUntilChanged()

    private fun getCalendarHeader(userId: Int): StateFlow<FeatureState<CalendarHeaderState>> = flow {
        emit(FeatureState.Loading)

        try {
            val today = LocalDate.now()
            val currentMonday = today.with(DayOfWeek.MONDAY)

            val startDate = currentMonday.minusWeeks(13)
            val endDate = currentMonday.plusWeeks(13)

            val totalWeeks = 26
            val calendarDays = (0 until (totalWeeks * 7)).map {
                startDate.plusDays(it.toLong())
            }

            val availableDays = withVisibleLoading {
                getCalendarAvailableDaysUseCase(
                    userId, startDate.toString(), endDate.toString())
            }

            emit(
                FeatureState.Success(
                    CalendarHeaderState(
                        config = CalendarConfig(
                            userId = userId,
                            startDate = startDate,
                            endDate = endDate,
                            totalWeeks = totalWeeks,
                            initialWeekPage = totalWeeks / 2,
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
    val calendarHeader: StateFlow<FeatureState<CalendarHeaderState>> = userIdFlow
        .filterNotNull()
        .flatMapLatest { userId -> getCalendarHeader(userId) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, FeatureState.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    val calendarEvents: StateFlow<FeatureState<CalendarEvents>> =
        paramsFlow
            .flatMapLatest { (userId, day, slot) ->
                flow {
                    val startEnd = day.format(dateFmt)
                    val key = cacheKey(userId, day, slot)

                    cache[key]?.let { emit(it) }

                    if(cache[key] !is FeatureState.Success) {
                        emit(FeatureState.Loading)
                    }

                    val result = withVisibleLoading {
                        getCalendarEventsUseCase(
                            startDate = startEnd,
                            endDate = startEnd,
                            userId = userId,
                            slotDuration = slot
                        )
                    }

                    cache[key] = result
                    emit(result)

                    if(result is FeatureState.Success) {
                        syncBlockedSelection(day, result.data)
                    }

                }.catch { e ->
                    emit(FeatureState.Error(e))
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FeatureState.Loading
            )

    private fun Set<LocalDateTime>.withoutDay(day: LocalDate): Set<LocalDateTime> =
        this.filterNot { it.toLocalDate() == day }.toSet()

    private fun syncBlockedSelection(day: LocalDate, events: CalendarEvents) {
        val blocked = events.blockedStartLocale()
        _defaultBlockedStartLocale.update { current ->
            current
                .withoutDay(day)
                .plus(blocked)
        }
        _selectedStartLocale.update { current ->
            current
                .withoutDay(day)
                .plus(blocked)
        }
    }

    fun createOwnClientAppointment(ownClient: AppointmentOwnClientCreate) {
        viewModelScope.launch {
            _isSaving.value = true

            val result = withVisibleLoading {
                createOwnClientAppointmentUseCase(ownClient)
            }

            result
                .onFailure { e ->
                    Timber.tag("Appointments").e("ERROR: on blocking appointments $e")
                    _isSaving.value = false
                }
                .onSuccess {
                    refreshCalendarEvents()
                    _isSaving.value = false
                }
        }
    }

    fun blockAppointments(message: String) {
        viewModelScope.launch {
            _isSaving.value = true

            val state = calendarEvents.value

            if(state !is FeatureState.Success) {
                _isSaving.value = false
                return@launch
            }

            val dayKey: LocalDate = _selectedDay.value ?: run {
                _isSaving.value = false
                return@launch
            }

            val day = state.data.days.firstOrNull { it.day == dayKey.toString() } ?: run {
                _isSaving.value = false
                return@launch
            }

            val slotsDiff: Set<LocalDateTime> =
                _selectedStartLocale.value - _defaultBlockedStartLocale.value

            val slotsToBlock = day.slots
                .filter { it.startDateLocale in slotsDiff }
                .map { slot ->
                    AppointmentBlockSlots(
                        startDate = slot.startDateUtc,
                        endDate = slot.endDateUtc
                    )
                }

            val result = withVisibleLoading {
                createBlockAppointmentsUseCase(
                    request = AppointmentBlockRequest(
                        message = message,
                        slots = slotsToBlock
                    )
                )
            }

            result
                .onFailure { e ->
                    Timber.tag("Appointments").e("ERROR: on blocking appointments $e")
                    _isSaving.value = false
                }
                .onSuccess {
                    refreshCalendarEvents()
                    _isSaving.value = false
                }
        }
    }

    fun loadCurrencies() {
        viewModelScope.launch {
            _currenciesState.value = FeatureState.Loading

            val result = withVisibleLoading {
                val userId = authDataStore.getUserId().firstOrNull()
                    ?: throw IllegalStateException("User Id not found in authDataStore")

                getUserCurrenciesUseCase(userId)
            }

            result
                .onSuccess { response ->
                    _currenciesState.value = FeatureState.Success(response)
                }
                .onFailure { e ->
                    Timber.tag("Currencies").e("ERROR: on Fetching Currencies in MyProducts $e")
                    _currenciesState.value = FeatureState.Error()
                }
        }
    }

    fun loadServices() {
        viewModelScope.launch {
            _servicesState.value = FeatureState.Loading

            val result = withVisibleLoading {
                val businessId = authDataStore.getBusinessId().firstOrNull()
                    ?: throw IllegalStateException("Business Id not found in authDataStore")

                getServicesByBusinessId(businessId)
            }

            result
                .onSuccess { response ->
                    _servicesState.value = FeatureState.Success(response)
                }
                .onFailure { e ->
                    Timber.tag("Services").e("ERROR: on Fetching Services in MyProducts $e")
                    _servicesState.value = FeatureState.Error()
                }
        }
    }

    fun setSelectedOwnClient(calendarEvents: CalendarEventsSlot) {
        _selectedOwnClient.value = calendarEvents
    }

    fun resetOwnClient() {
        _selectedOwnClient.value = null
    }

    fun setBlockDate(startDate: LocalDateTime) {
        _selectedStartLocale.update { current ->
            if(startDate in current) current -startDate else current + startDate
        }
    }

    fun resetSelectedLocalDates() {
        _selectedStartLocale.value = _defaultBlockedStartLocale.value
    }

    fun setDay(day: LocalDate) {
        _selectedDay.value = day
    }

    fun setSlotDuration(duration: String?) {
        if(duration?.isNotEmpty() == true) {
            _slotDuration.value = duration.toInt()
        }
    }

    fun refreshCalendarEvents() {
        val day = selectedDay.value ?: return
        viewModelScope.launch {
            val userId = authDataStore.getUserId().firstOrNull() ?: return@launch
            val key = cacheKey(userId, day, _slotDuration.value)
            cache.remove(key)
            refreshTick.update { it + 1 }
        }
    }
}
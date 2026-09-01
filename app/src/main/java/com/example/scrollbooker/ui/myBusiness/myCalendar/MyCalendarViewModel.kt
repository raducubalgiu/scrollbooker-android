package com.example.scrollbooker.ui.myBusiness.myCalendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.snackbar.SnackBarUiEvent
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentBlockRequest
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentBlockSlots
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentLastMinuteRequest
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentOwnClientCreate
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.CreateBlockAppointmentsUseCase
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.CreateLastMinuteAppointmentUseCase
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.CreateOwnClientAppointmentUseCase
import com.example.scrollbooker.entity.booking.availability.domain.model.CalendarEvents
import com.example.scrollbooker.entity.booking.availability.domain.model.CalendarEventsSlot
import com.example.scrollbooker.entity.booking.availability.domain.model.blockedStartLocale
import com.example.scrollbooker.entity.booking.availability.domain.useCase.GetCalendarAvailableDaysUseCase
import com.example.scrollbooker.entity.booking.availability.domain.useCase.GetUserCalendarEventsUseCase
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.entity.booking.schedule.domain.useCase.GetSchedulesByUserIdUseCase
import com.example.scrollbooker.store.AuthDataStore
import com.example.scrollbooker.components.customized.calendar.CalendarConfig
import com.example.scrollbooker.components.customized.calendar.CalendarHeaderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import timber.log.Timber
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@HiltViewModel
class MyCalendarViewModel @Inject constructor(
    authDataStore: AuthDataStore,
    private val getSchedulesByUserIdUseCase: GetSchedulesByUserIdUseCase,
    private val getCalendarAvailableDaysUseCase: GetCalendarAvailableDaysUseCase,
    private val getCalendarEventsUseCase: GetUserCalendarEventsUseCase,
    private val createBlockAppointmentsUseCase: CreateBlockAppointmentsUseCase,
    private val createOwnClientAppointmentUseCase: CreateOwnClientAppointmentUseCase,
    private val createLastMinuteAppointmentUseCase: CreateLastMinuteAppointmentUseCase
): ViewModel() {
    private val _selectedDay = MutableStateFlow<LocalDate?>(LocalDate.now())
    val selectedDay: StateFlow<LocalDate?> = _selectedDay.asStateFlow()

    private val _isBlocking = MutableStateFlow<Boolean>(false)
    val isBlocking: StateFlow<Boolean> = _isBlocking

    private val _defaultBlockedStartLocale = MutableStateFlow<Set<LocalDateTime>>(emptySet())
    val defaultBlockedStartLocale: StateFlow<Set<LocalDateTime>> = _defaultBlockedStartLocale.asStateFlow()

    private val _selectedStartLocale = MutableStateFlow<Set<LocalDateTime>>(emptySet())
    val selectedStartLocale: StateFlow<Set<LocalDateTime>> = _selectedStartLocale.asStateFlow()

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val _slotDuration = MutableStateFlow<Int>(60)
    val slotDuration: MutableStateFlow<Int> = _slotDuration

    private val _selectedOwnClient = MutableStateFlow<CalendarEventsSlot?>(null)
    val selectedOwnClient: StateFlow<CalendarEventsSlot?> = _selectedOwnClient.asStateFlow()

    private val _events = MutableSharedFlow<SnackBarUiEvent.Show>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<SnackBarUiEvent.Show> = _events.asSharedFlow()

    // Bumped only when an own-client/last-minute/block action actually succeeds, so the
    // sheets can close themselves on real success instead of on every isSaving->false toggle
    // (which used to close the sheet on failure too, silently).
    private val _actionSucceededTick = MutableStateFlow(0)
    val actionSucceededTick: StateFlow<Int> = _actionSucceededTick.asStateFlow()

    private val refreshTick = MutableStateFlow(0)
    private val userIdFlow: Flow<Int?> = authDataStore.getUserId().distinctUntilChanged()
    val userId: StateFlow<Int?> = userIdFlow.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    private val businessIdFlow: Flow<Int?> = authDataStore.getBusinessId().distinctUntilChanged()
    private val businessOwnerIdFlow: Flow<Int?> = authDataStore.getBusinessOwnerId().distinctUntilChanged()

    private val employeeIdFlow: Flow<Int?> = combine(
        businessOwnerIdFlow,
        userIdFlow
    ) { businessOwnerId, userId ->
        if (businessOwnerId != null && userId != null && businessOwnerId != userId) userId else null
    }.distinctUntilChanged()

    private val dateFmt = DateTimeFormatter.ISO_LOCAL_DATE
    private val cache = ConcurrentHashMap<String, FeatureState<CalendarEvents>>()

    private fun cacheKey(userId: Int, businessId: Int, employeeId: Int?, day: LocalDate, slot: Int): String =
        "$userId:$businessId:${employeeId ?: "-"}:${day.format(dateFmt)}:$slot"

    // Shared availability context (userId/businessId/employeeId/slotDuration) that both the
    // header (26-week overview) and the day events flow are keyed on, so they never drift apart.
    private val calendarContextFlow: Flow<CalendarHeaderParams> =
        combine(
            userIdFlow.filterNotNull(),
            businessIdFlow.filterNotNull(),
            employeeIdFlow,
            slotDuration
        ) { userId, businessId, employeeId, slot ->
            CalendarHeaderParams(userId, businessId, employeeId, slot)
        }.distinctUntilChanged()

    private val paramsFlow: Flow<CalendarParams> =
        combine(
            calendarContextFlow,
            selectedDay.filterNotNull(),
            refreshTick
        ) { context, day, refresh ->
            CalendarParams(
                userId = context.userId,
                businessId = context.businessId,
                employeeId = context.employeeId,
                day = day,
                slot = context.slotDuration,
                refresh = refresh
            )
        }.distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    val calendarHeader: StateFlow<FeatureState<CalendarHeaderState>> = calendarContextFlow
        .flatMapLatest { params ->
            flow {
                emit(FeatureState.Loading)

                val today = LocalDate.now()
                val currentMonday = today.with(DayOfWeek.MONDAY)

                val startDate = currentMonday.minusWeeks(13)
                val endDate = currentMonday.plusWeeks(13)

                val totalWeeks = 26
                val calendarDays = (0 until (totalWeeks * 7)).map {
                    startDate.plusDays(it.toLong())
                }

                val result = withVisibleLoading {
                    getCalendarAvailableDaysUseCase(
                        businessId = params.businessId,
                        employeeId = params.employeeId,
                        startDate = startDate.toString(),
                        endDate = endDate.toString(),
                        slotDuration = params.slotDuration
                    )
                }

                result.fold(
                    onSuccess = { availableDays ->
                        emit(
                            FeatureState.Success(
                                CalendarHeaderState(
                                    config = CalendarConfig(
                                        userId = params.userId,
                                        startDate = startDate,
                                        endDate = endDate,
                                        totalWeeks = totalWeeks,
                                        initialWeekPage = totalWeeks / 2,
                                        initialDayPage = today.dayOfWeek.ordinal,
                                        selectedDay = today
                                    ),
                                    calendarDays = calendarDays,
                                    calendarAvailableDays = availableDays.map { LocalDate.parse(it) },
                                )
                            )
                        )
                    },
                    onFailure = { e -> emit(FeatureState.Error(e)) }
                )
            }.catch { e ->
                emit(FeatureState.Error(e))
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, FeatureState.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val schedules: StateFlow<FeatureState<List<Schedule>>> = userIdFlow
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { userId ->
            flow {
                emit(FeatureState.Loading)

                val result = getSchedulesByUserIdUseCase(userId)

                emit(
                    result.fold(
                        onSuccess = { FeatureState.Success(it) },
                        onFailure = { e ->
                            Timber.tag("Schedules").e("ERROR: on Fetching Schedules By User Id $e")
                            FeatureState.Error(e)
                        }
                    )
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, FeatureState.Loading)

    val daySchedule: StateFlow<Schedule?> = combine(
        schedules,
        selectedDay.filterNotNull()
    ) { schedulesState, day ->
        val allSchedules = (schedulesState as? FeatureState.Success)?.data ?: return@combine null
        val dayName = day.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        allSchedules.firstOrNull { it.dayOfWeek == dayName }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val calendarEvents: StateFlow<FeatureState<CalendarEvents>> =
        paramsFlow.flatMapLatest { p ->
            flow {
                val startEnd = p.day.format(dateFmt)
                val key = cacheKey(p.userId, p.businessId, p.employeeId, p.day, p.slot)

                val cached = cache[key]

                if(cached is FeatureState.Success) {
                    emit(cached)
                } else {
                    emit(FeatureState.Loading)
                }

                if (cache[key] is FeatureState.Success) {
                    return@flow
                }

                val result = withVisibleLoading {
                    getCalendarEventsUseCase(
                        businessId = p.businessId,
                        employeeId = p.employeeId,
                        startDate = startEnd,
                        endDate = startEnd,
                        slotDuration = p.slot
                    )
                }

                val state = result.fold(
                    onSuccess = { FeatureState.Success(it) },
                    onFailure = { e ->
                        Timber.tag("Calendar").e("ERROR: on Fetching User Calendar Events $e")
                        FeatureState.Error(e)
                    }
                )

                cache[key] = state
                emit(state)

                if(state is FeatureState.Success) {
                    syncBlockedSelection(p.day, state.data)
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

    fun createOwnClientAppointment(request: AppointmentOwnClientCreate) {
        viewModelScope.launch {
            _isSaving.value = true

            val result = withVisibleLoading {
                createOwnClientAppointmentUseCase(request)
            }

            result
                .onFailure { e ->
                    Timber.tag("Appointments").e("ERROR: on creating own client appointment $e")
                    _events.tryEmit(SnackBarUiEvent.somethingWentWrong())
                    _isSaving.value = false
                }
                .onSuccess {
                    refreshCurrentDay()
                    _actionSucceededTick.update { it + 1 }
                    _isSaving.value = false
                }
        }
    }

    fun createLastMinute(request: AppointmentLastMinuteRequest) {
        viewModelScope.launch {
            _isSaving.value = true

            val result = withVisibleLoading {
                createLastMinuteAppointmentUseCase(request)
            }

            result
                .onFailure { e ->
                    Timber.tag("Appointments").e("ERROR: on creating last minute appointments $e")
                    _events.tryEmit(SnackBarUiEvent.somethingWentWrong())
                    _isSaving.value = false
                }
                .onSuccess {
                    refreshCurrentDay()
                    _actionSucceededTick.update { it + 1 }
                    _isSaving.value = false
                }
        }
    }

    fun blockAppointments(message: String) {
        viewModelScope.launch {
            _isSaving.value = true

            val userId = userIdFlow.first() ?: run {
                _isSaving.value = false
                return@launch
            }

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
                        endDate = slot.endDateUtc,
                        userId = userId
                    )
                }

            val result = withVisibleLoading {
                createBlockAppointmentsUseCase(
                    request = AppointmentBlockRequest(
                        blockedMessage = message,
                        slots = slotsToBlock
                    )
                )
            }

            result
                .onFailure { e ->
                    Timber.tag("Appointments").e("ERROR: on blocking appointments $e")
                    _events.tryEmit(SnackBarUiEvent.somethingWentWrong())
                    _isSaving.value = false
                }
                .onSuccess { created ->
                    refreshCurrentDay()
                    resetSelectedLocalDates()
                    _actionSucceededTick.update { it + 1 }
                    _isSaving.value = false
                }
        }
    }

    fun setSelectedOwnClient(calendarEvents: CalendarEventsSlot?) {
        _selectedOwnClient.value = calendarEvents
    }

    fun toggleBlocking() {
        _isBlocking.update { !it }
    }

    fun setBlockDate(startDate: LocalDateTime) {
        _selectedStartLocale.update { current ->
            if(startDate in current) current -startDate else current + startDate
        }
    }

    fun resetSelectedLocalDates() {
        _selectedStartLocale.value = _defaultBlockedStartLocale.value
        _isBlocking.value = false
    }

    fun setDay(day: LocalDate) {
        _selectedDay.value = day
    }

    fun setSlotDuration(duration: String?) {
        if(duration?.isNotEmpty() == true) {
            _slotDuration.value = duration.toInt()
        }
    }

    suspend fun refreshCurrentDay() {
        val context = calendarContextFlow.first()
        val day = selectedDay.value ?: return
        val key = cacheKey(context.userId, context.businessId, context.employeeId, day, context.slotDuration)

        cache.remove(key)
        refreshTick.update { it + 1 }
    }
}
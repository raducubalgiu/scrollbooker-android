package com.example.scrollbooker.ui.myBusiness.myCalendar

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
import com.example.scrollbooker.entity.booking.availability.domain.useCase.GetBusinessEmployeesCalendarEventsByDayUseCase
import com.example.scrollbooker.entity.booking.availability.domain.useCase.GetCalendarAvailableDaysUseCase
import com.example.scrollbooker.entity.booking.availability.domain.useCase.GetUserCalendarEventsUseCase
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.entity.booking.employee.domain.useCase.GetAllEmployeesByOwnerUseCase
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.entity.booking.schedule.domain.useCase.GetSchedulesByUserIdUseCase
import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.store.AuthDataStore
import com.example.scrollbooker.components.customized.calendar.BaseCalendarViewModel
import com.example.scrollbooker.components.customized.calendar.CalendarContext
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
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
    getCalendarAvailableDaysUseCase: GetCalendarAvailableDaysUseCase,
    private val getCalendarEventsUseCase: GetUserCalendarEventsUseCase,
    private val createBlockAppointmentsUseCase: CreateBlockAppointmentsUseCase,
    private val createOwnClientAppointmentUseCase: CreateOwnClientAppointmentUseCase,
    private val createLastMinuteAppointmentUseCase: CreateLastMinuteAppointmentUseCase,
    private val getAllEmployeesByOwnerUseCase: GetAllEmployeesByOwnerUseCase,
    private val getBusinessEmployeesCalendarEventsByDayUseCase: GetBusinessEmployeesCalendarEventsByDayUseCase
): BaseCalendarViewModel(getCalendarAvailableDaysUseCase) {
    private val _selectedDay = MutableStateFlow<LocalDate?>(LocalDate.now())
    val selectedDay: StateFlow<LocalDate?> = _selectedDay.asStateFlow()

    private val _defaultBlockedStartLocale = MutableStateFlow<Set<LocalDateTime>>(emptySet())
    val defaultBlockedStartLocale: StateFlow<Set<LocalDateTime>> = _defaultBlockedStartLocale.asStateFlow()

    private val _selectedStartLocale = MutableStateFlow<Set<LocalDateTime>>(emptySet())
    val selectedStartLocale: StateFlow<Set<LocalDateTime>> = _selectedStartLocale.asStateFlow()

    private val _slotDuration = MutableStateFlow<Int>(60)
    val slotDuration: MutableStateFlow<Int> = _slotDuration

    private val _selectedOwnClient = MutableStateFlow<CalendarEventsSlot?>(null)
    val selectedOwnClient: StateFlow<CalendarEventsSlot?> = _selectedOwnClient.asStateFlow()

    private val _isBlocking = MutableStateFlow<Boolean>(false)
    val isBlocking: StateFlow<Boolean> = _isBlocking

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    // True only while calendarEvents is silently re-fetching a day that already has data
    // (stale refresh) - drives the pull-to-refresh spinner without forcing a full loading state.
    private val _isRefreshingCurrentDay = MutableStateFlow(false)
    val isRefreshingCurrentDay: StateFlow<Boolean> = _isRefreshingCurrentDay.asStateFlow()

    private val _events = MutableSharedFlow<SnackBarUiEvent.Show>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<SnackBarUiEvent.Show> = _events.asSharedFlow()

    private val _actionSucceededTick = MutableStateFlow(0)
    val actionSucceededTick: StateFlow<Int> = _actionSucceededTick.asStateFlow()

    private val refreshTick = MutableStateFlow(0)
    private val userIdFlow: Flow<Int?> = authDataStore.getUserId().distinctUntilChanged()
    val userId: StateFlow<Int?> = userIdFlow.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    private val businessIdFlow: Flow<Int?> = authDataStore.getBusinessId().distinctUntilChanged()
    private val businessOwnerIdFlow: Flow<Int?> = authDataStore.getBusinessOwnerId().distinctUntilChanged()

    private val hasEmployeesFlow: Flow<Boolean> = authDataStore.getHasEmployees()
        .map { it == true }
        .distinctUntilChanged()

    // Own identity (not the currently viewed employee's) - used by the header's employee slot
    // when there's no one else to switch to (solo employee, or business without employees).
    val ownFullName: StateFlow<String?> = authDataStore.getUserFullName()
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val ownAvatar: StateFlow<String?> = authDataStore.getUserAvatar()
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    // Whether the logged-in user IS the business owner (as opposed to being an employee
    // themselves) - only an owner with employees needs to pick which employee's calendar to view.
    private val isOwnerFlow: Flow<Boolean> = combine(
        businessOwnerIdFlow.filterNotNull(),
        userIdFlow.filterNotNull()
    ) { businessOwnerId, userId -> businessOwnerId == userId }.distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    val employees: StateFlow<FeatureState<List<Employee>>> = combine(
        isOwnerFlow,
        hasEmployeesFlow
    ) { isOwner, hasEmployees -> isOwner && hasEmployees }
        .distinctUntilChanged()
        .flatMapLatest { shouldLoad ->
            if (!shouldLoad) {
                flowOf(FeatureState.Success(emptyList()))
            } else {
                flow {
                    emit(FeatureState.Loading)

                    val ownerId = businessOwnerIdFlow.first()
                    if (ownerId == null) {
                        emit(FeatureState.Error(IllegalStateException("Missing businessOwnerId")))
                        return@flow
                    }

                    val result = runSuspendCatching { getAllEmployeesByOwnerUseCase(ownerId) }

                    emit(
                        result.fold(
                            onSuccess = { FeatureState.Success(it) },
                            onFailure = { e ->
                                Timber.tag("Employees").e("ERROR: on Fetching Employees $e")
                                FeatureState.Error(e)
                            }
                        )
                    )
                }
            }
        }
        .catch { e -> emit(FeatureState.Error(e)) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, FeatureState.Loading)

    private val _selectedEmployeeId = MutableStateFlow<Int?>(null)
    val selectedEmployeeId: StateFlow<Int?> = _selectedEmployeeId.asStateFlow()

    val selectedEmployee: StateFlow<Employee?> = combine(
        employees,
        _selectedEmployeeId
    ) { state, selectedId ->
        (state as? FeatureState.Success)?.data?.firstOrNull { it.id == selectedId }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        // Defaults to the first employee once the list loads, so the calendar isn't left blank
        // for an owner-with-employees who hasn't explicitly picked one yet.
        employees
            .onEach { state ->
                if (state is FeatureState.Success && _selectedEmployeeId.value == null) {
                    state.data.firstOrNull()?.let { _selectedEmployeeId.value = it.id }
                }
            }
            .launchIn(viewModelScope)
    }

    fun selectEmployee(employeeId: Int) {
        _selectedEmployeeId.value = employeeId
    }

    // - Employee themselves (not the owner): always their own calendar.
    // - Owner with employees: must resolve to whichever employee is currently selected.
    // - Owner without employees: their own calendar, same as before.
    private val employeeIdFlow: Flow<Int?> = combine(
        businessOwnerIdFlow,
        userIdFlow,
        hasEmployeesFlow,
        _selectedEmployeeId
    ) { businessOwnerId, userId, hasEmployees, selectedEmployeeId ->
        when {
            businessOwnerId == null || userId == null -> null
            businessOwnerId != userId -> userId
            hasEmployees -> selectedEmployeeId
            else -> null
        }
    }.distinctUntilChanged()

    private val dateFmt = DateTimeFormatter.ISO_LOCAL_DATE
    private val cache = ConcurrentHashMap<String, FeatureState<CalendarEvents>>()

    // Keys pending a forced re-fetch (via refreshCurrentDay) whose cached value must still be
    // kept around and shown while that re-fetch is in flight, so the screen never blanks out.
    private val staleKeys = ConcurrentHashMap.newKeySet<String>()

    private fun cacheKey(userId: Int, businessId: Int, employeeId: Int?, day: LocalDate, slot: Int): String =
        "$userId:$businessId:${employeeId ?: "-"}:${day.format(dateFmt)}:$slot"

    override val calendarContextFlow: Flow<CalendarContext> =
        combine(
            userIdFlow.filterNotNull(),
            businessIdFlow.filterNotNull(),
            employeeIdFlow,
            slotDuration
        ) { userId, businessId, employeeId, slot ->
            CalendarContext(userId, businessId, employeeId, slot)
        }.distinctUntilChanged()

    override fun calendarWindow(currentMonday: LocalDate): Pair<LocalDate, LocalDate> =
        currentMonday.minusWeeks(13) to currentMonday.plusWeeks(13)

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

    // Whichever person's calendar is actually being displayed - the auth user themselves
    // (employee or owner-without-employees), or the currently selected employee when the owner
    // has employees. Schedules, and any write action (block/own-client/etc.), must target this,
    // not the raw auth user, or an owner viewing an employee's calendar would read/write that
    // employee's calendar using their own id instead of the employee's.
    private val calendarTargetUserIdFlow: Flow<Int?> = combine(
        userIdFlow,
        employeeIdFlow
    ) { userId, employeeId -> employeeId ?: userId }.distinctUntilChanged()

    // Public read of the same resolution, for screens (e.g. AddOwnClientScreen) that need to
    // scope their own requests/fetches to whichever calendar is currently displayed.
    val calendarTargetUserId: StateFlow<Int?> =
        calendarTargetUserIdFlow.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val schedules: StateFlow<FeatureState<List<Schedule>>> = calendarTargetUserIdFlow
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { targetUserId ->
            flow {
                emit(FeatureState.Loading)

                val result = getSchedulesByUserIdUseCase(targetUserId)

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

    // Widest [start, end] across every employee of the business for the selected day - only
    // relevant when the business has employees. Used so the calendar's visible time range stays
    // consistent across employee switches instead of resizing to each one's own (possibly
    // shorter) schedule; the gap between an employee's own hours and this wider window is what
    // gets rendered as a "Closed" block on the timeline.
    @OptIn(ExperimentalCoroutinesApi::class)
    val businessDayWindow: StateFlow<Pair<LocalTime, LocalTime>?> = combine(
        hasEmployeesFlow,
        selectedDay.filterNotNull(),
        slotDuration
    ) { hasEmployees, day, duration -> Triple(hasEmployees, day, duration) }
        .distinctUntilChanged()
        .flatMapLatest { (hasEmployees, day, duration) ->
            if (!hasEmployees) {
                flowOf<Pair<LocalTime, LocalTime>?>(null)
            } else {
                flow {
                    val result = getBusinessEmployeesCalendarEventsByDayUseCase(day.toString(), duration)

                    val window = result.fold(
                        onSuccess = { businessDay ->
                            val allSlots = businessDay.employees.flatMap { it.slots }
                            val start = allSlots.mapNotNull { it.startDateLocale?.toLocalTime() }.minOrNull()
                            val end = allSlots.mapNotNull { it.endDateLocale?.toLocalTime() }.maxOrNull()
                            if (start != null && end != null) start to end else null
                        },
                        onFailure = { e ->
                            Timber.tag("Calendar").e("ERROR: on Fetching Business Employees Calendar Events $e")
                            null
                        }
                    )

                    emit(window)
                }
            }
        }
        .catch { emit(null) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val calendarEvents: StateFlow<FeatureState<CalendarEvents>> =
        paramsFlow.flatMapLatest { p ->
            flow {
                val startEnd = p.day.format(dateFmt)
                val key = cacheKey(p.userId, p.businessId, p.employeeId, p.day, p.slot)

                val cached = cache[key]
                val isStale = staleKeys.contains(key)

                if(cached is FeatureState.Success) {
                    // Keep showing the last known data (even if stale) instead of blanking the
                    // screen with a full loading state while a forced refresh is in flight.
                    emit(cached)
                } else {
                    emit(FeatureState.Loading)
                }

                if (cached is FeatureState.Success && !isStale) {
                    return@flow
                }

                if (isStale) {
                    _isRefreshingCurrentDay.value = true
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

                staleKeys.remove(key)

                // Only cache successes - a transient failure during a background refresh
                // shouldn't poison the last known good data for this day.
                if (state is FeatureState.Success) {
                    cache[key] = state
                }

                emit(state)

                if(state is FeatureState.Success) {
                    syncBlockedSelection(p.day, state.data)
                }

            }
            .onCompletion { _isRefreshingCurrentDay.value = false }
            .catch { e ->
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

        // Marked stale (not removed) so calendarEvents keeps showing the current data while it
        // silently re-fetches, instead of blanking the whole day out to a loading state.
        staleKeys.add(key)
        refreshTick.update { it + 1 }
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

            // Must target whichever calendar is actually displayed (the selected employee, when
            // the owner has employees), not the raw auth user - the backend checks
            // _is_slot_booked against exactly this id per slot ("Provider ocupat" otherwise).
            val userId = calendarTargetUserIdFlow.first() ?: run {
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
}
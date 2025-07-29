package com.example.scrollbooker.ui.sharedModules.calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.calendar.domain.model.AvailableDay
import com.example.scrollbooker.entity.booking.calendar.domain.useCase.GetCalendarAvailableDaysUseCase
import com.example.scrollbooker.entity.booking.calendar.domain.useCase.GetUserAvailableTimeslotsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getCalendarAvailableDaysUseCase: GetCalendarAvailableDaysUseCase,
    private val getUserAvailableTimeslotsUseCase: GetUserAvailableTimeslotsUseCase
): ViewModel() {

    private val userId = MutableStateFlow<Int?>(null)
    private val selectedDay = MutableStateFlow<LocalDate?>(LocalDate.now())
    private val slotDuration = MutableStateFlow<Int?>(null)

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
        slotDuration.filterNotNull()
    ) {
        day, userId, slotDuration ->
        Triple(day, userId, slotDuration)
    }
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { (day, userId, slotDuration) ->
            flow {
                emit(FeatureState.Loading)

                val response = withVisibleLoading {
                    getUserAvailableTimeslotsUseCase(
                        day = day.toString(),
                        userId = userId,
                        slotDuration = slotDuration
                    )
                }

                emit(response)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            FeatureState.Loading
        )

    fun setUserId(id: Int) {
        userId.value = id
    }

    fun setDay(day: LocalDate) {
        selectedDay.value = day
    }

    fun setSlotDuration(duration: Int) {
        slotDuration.value = duration
    }


//    private val _availableDays = MutableStateFlow<FeatureState<List<LocalDate>>>(FeatureState.Loading)
//    val availableDays: StateFlow<FeatureState<List<LocalDate>>> = _availableDays
//
//    private val _calendarDays = MutableStateFlow<List<LocalDate>>(emptyList())
//    val calendarDays: StateFlow<List<LocalDate>> = _calendarDays
//
//    private val _availableDay = MutableStateFlow<FeatureState<AvailableDay>>(FeatureState.Loading)
//    val availableDay: StateFlow<FeatureState<AvailableDay>> = _availableDay
//
//    private val _calendarConfig = MutableStateFlow<CalendarConfig?>(null)
//    val calendarConfig: StateFlow<CalendarConfig?> = _calendarConfig
//
//    private val _selectedSlot = MutableStateFlow<Slot?>(null)
//    val selectedSlot: StateFlow<Slot?> = _selectedSlot
//
//    fun toggleSlot(slot: Slot?) {
//        _selectedSlot.value = slot
//    }
//
//    fun updateSelectedDay(newDay: LocalDate) {
//        _calendarConfig.update { current ->
//            current?.copy(selectedDay = newDay)
//        }
//    }
//
//    fun setCalendarConfig(
//        userId: Int,
//        selectedDay: LocalDate = LocalDate.now(),
//        totalWeeks: Int = 13
//    ) {
//
//        val today = LocalDate.now()
//        val startOfCalendar = today.with(DayOfWeek.MONDAY)
//        val endDate = startOfCalendar.plusWeeks(totalWeeks.toLong())
//
//        val calendarDates = (0 until totalWeeks * 7).map { startOfCalendar.plusDays(it.toLong()) }
//        _calendarDays.value = calendarDates
//
//        val selectedDayIndex = calendarDates.indexOfFirst { it == selectedDay }.coerceAtLeast(0)
//        val initialWeekPage = selectedDayIndex / 7
//        val initialDayPage = selectedDay.dayOfWeek.ordinal
//
//        _calendarConfig.value = CalendarConfig(
//            userId = userId,
//            startDate = startOfCalendar,
//            endDate = endDate,
//            totalWeeks = totalWeeks,
//            initialWeekPage = initialWeekPage,
//            initialDayPage = initialDayPage,
//            selectedDay = selectedDay
//        )
//
//        loadAvailableDays(userId, startOfCalendar, endDate)
//    }
//
//    fun loadAvailableDays(
//        userId: Int,
//        startDate: LocalDate,
//        endDate: LocalDate
//    ) {
//        viewModelScope.launch {
//            _availableDays.value = FeatureState.Loading
//
//            val result: Result<List<String>> = runCatching {
//                withVisibleLoading {
//                    getCalendarAvailableDaysUseCase(
//                        userId = userId,
//                        startDate = startDate.toString(),
//                        endDate = endDate.toString()
//                    )
//                }
//            }
//
//            _availableDays.value = result.fold(
//                onSuccess = { days -> FeatureState.Success(days.map {LocalDate.parse(it) }) },
//                onFailure = { FeatureState.Error(it) }
//            )
//        }
//    }
//
//    fun loadUserAvailableTimeslots(day: LocalDate, userId: Int, slotDuration: Int) {
//        viewModelScope.launch {
//            _availableDay.value = FeatureState.Loading
//
//            val response = withVisibleLoading {
//                getUserAvailableTimeslotsUseCase(
//                    day = day.toString(),
//                    userId = userId,
//                    slotDuration = slotDuration
//                )
//            }
//
//            _availableDay.value = response
//        }
//    }
}
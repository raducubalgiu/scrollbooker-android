package com.example.scrollbooker.modules.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.calendar.domain.model.AvailableDay
import com.example.scrollbooker.entity.calendar.domain.useCase.GetCalendarAvailableDaysUseCase
import com.example.scrollbooker.entity.calendar.domain.useCase.GetUserAvailableTimeslotsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getCalendarAvailableDaysUseCase: GetCalendarAvailableDaysUseCase,
    private val getUserAvailableTimeslotsUseCase: GetUserAvailableTimeslotsUseCase
): ViewModel() {
    private val _availableDays = MutableStateFlow<FeatureState<List<LocalDate>>>(FeatureState.Loading)
    val availableDays: StateFlow<FeatureState<List<LocalDate>>> = _availableDays

    private val _calendarDays = MutableStateFlow<List<LocalDate>>(emptyList())
    val calendarDays: StateFlow<List<LocalDate>> = _calendarDays

    private val _availableDay = MutableStateFlow<FeatureState<AvailableDay>>(FeatureState.Loading)
    val availableDay: StateFlow<FeatureState<AvailableDay>> = _availableDay

    private val _calendarConfig = MutableStateFlow<CalendarConfig?>(null)
    val calendarConfig: StateFlow<CalendarConfig?> = _calendarConfig

    fun setCalendarConfig(
        userId: Int,
        selectedDay: LocalDate = LocalDate.now(),
        totalWeeks: Int = 13
    ) {
        val today = LocalDate.now()
        val startOfCalendar = today.with(DayOfWeek.MONDAY)
        val endDate = startOfCalendar.plusWeeks(totalWeeks.toLong())

        val calendarDates = (0 until totalWeeks * 7).map { startOfCalendar.plusDays(it.toLong()) }
        _calendarDays.value = calendarDates

        val selectedDayIndex = calendarDates.indexOfFirst { it == selectedDay }.coerceAtLeast(0)
        val initialWeekPage = selectedDayIndex / 7
        val initialDayPage = selectedDay.dayOfWeek.ordinal

        _calendarConfig.value = CalendarConfig(
            userId = userId,
            startDate = startOfCalendar,
            endDate = endDate,
            totalWeeks = totalWeeks,
            initialWeekPage = initialWeekPage,
            initialDayPage = initialDayPage,
            selectedDay = selectedDay
        )
        loadAvailableDays(userId, startOfCalendar, endDate)
    }

    fun loadAvailableDays(
        userId: Int,
        startDate: LocalDate,
        endDate: LocalDate
    ) {
        viewModelScope.launch {
            _availableDays.value = FeatureState.Loading

            val result: Result<List<String>> = runCatching {
                withVisibleLoading {
                    getCalendarAvailableDaysUseCase(
                        userId = userId,
                        startDate = startDate.toString(),
                        endDate = endDate.toString()
                    )
                }
            }

            _availableDays.value = result.fold(
                onSuccess = { days -> FeatureState.Success(days.map {LocalDate.parse(it) }) },
                onFailure = { FeatureState.Error(it) }
            )
        }
    }

    fun loadUserAvailableTimeslots(day: LocalDate, userId: Int, slotDuration: Int) {
        viewModelScope.launch {
            _availableDay.value = FeatureState.Loading

            val response = withVisibleLoading {
                getUserAvailableTimeslotsUseCase(
                    day = day.toString(),
                    userId = userId,
                    slotDuration = slotDuration
                )
            }

            _availableDay.value = response
        }
    }
}
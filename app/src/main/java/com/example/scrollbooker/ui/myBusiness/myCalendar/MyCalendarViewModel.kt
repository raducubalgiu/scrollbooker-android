package com.example.scrollbooker.ui.myBusiness.myCalendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.calendar.domain.useCase.GetCalendarAvailableDaysUseCase
import com.example.scrollbooker.store.AuthDataStore
import com.example.scrollbooker.ui.modules.calendar.CalendarConfig
import com.example.scrollbooker.ui.modules.calendar.CalendarHeaderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyCalendarViewModel @Inject constructor(
    private val getCalendarAvailableDaysUseCase: GetCalendarAvailableDaysUseCase,
    private val authDataStore: AuthDataStore
): ViewModel() {
    //private val userId = MutableStateFlow<Int?>(null)
    private val selectedDay = MutableStateFlow<LocalDate?>(LocalDate.now())

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
    val calendarHeader: StateFlow<FeatureState<CalendarHeaderState>> = authDataStore.getUserId()
        .filterNotNull()
        .flatMapLatest { userId ->
            getCalendarHeader(userId)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, FeatureState.Loading)

    fun setDay(day: LocalDate) {
        selectedDay.value = day
    }
}
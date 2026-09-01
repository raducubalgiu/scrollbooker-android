package com.example.scrollbooker.components.customized.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.availability.domain.useCase.GetCalendarAvailableDaysUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit
import timber.log.Timber

/**
 * Shared calendar-header logic for MyCalendarViewModel and BookingViewModel: same request
 * ([GetCalendarAvailableDaysUseCase]), same [CalendarHeaderState] shape. Subclasses only supply
 * their own [calendarContextFlow] (how businessId/employeeId/userId/slotDuration are obtained -
 * reactive from auth data store for MyCalendar, static + selected employee for Booking) and their
 * own visible [calendarWindow] (MyCalendar: 6 months back/forward; Booking: 6 months forward only).
 */
abstract class BaseCalendarViewModel(
    private val getCalendarAvailableDaysUseCase: GetCalendarAvailableDaysUseCase
) : ViewModel(), CalendarViewModelContract {

    protected abstract val calendarContextFlow: Flow<CalendarContext>

    protected abstract fun calendarWindow(currentMonday: LocalDate): Pair<LocalDate, LocalDate>

    @OptIn(ExperimentalCoroutinesApi::class)
    override val calendarHeader: StateFlow<FeatureState<CalendarHeaderState>> by lazy {
        calendarContextFlow
            .distinctUntilChanged()
            .flatMapLatest { context ->
                flow {
                    emit(FeatureState.Loading)

                    val today = LocalDate.now()
                    val currentMonday = today.with(DayOfWeek.MONDAY)
                    val (startDate, endDate) = calendarWindow(currentMonday)

                    val totalWeeks = ChronoUnit.WEEKS.between(startDate, endDate).toInt()
                    val initialWeekPage = ChronoUnit.WEEKS.between(startDate, currentMonday).toInt()
                    val calendarDays = (0 until (totalWeeks * 7)).map { startDate.plusDays(it.toLong()) }

                    val result = withVisibleLoading {
                        getCalendarAvailableDaysUseCase(
                            businessId = context.businessId,
                            employeeId = context.employeeId,
                            startDate = startDate.toString(),
                            endDate = endDate.toString(),
                            slotDuration = context.slotDuration
                        )
                    }

                    emit(
                        result.fold(
                            onSuccess = { availableDays ->
                                FeatureState.Success(
                                    CalendarHeaderState(
                                        config = CalendarConfig(
                                            userId = context.userId,
                                            startDate = startDate,
                                            endDate = endDate,
                                            totalWeeks = totalWeeks,
                                            initialWeekPage = initialWeekPage,
                                            initialDayPage = today.dayOfWeek.ordinal,
                                            selectedDay = today
                                        ),
                                        calendarDays = calendarDays,
                                        calendarAvailableDays = availableDays.map { LocalDate.parse(it) }
                                    )
                                )
                            },
                            onFailure = { e ->
                                Timber.tag("Calendar").e(e, "ERROR: on Fetching Calendar Header")
                                FeatureState.Error(e)
                            }
                        )
                    )
                }.catch { e ->
                    Timber.tag("Calendar").e(e, "Fatal Calendar Header Flow Exception")
                    emit(FeatureState.Error(e))
                }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, FeatureState.Loading)
    }
}

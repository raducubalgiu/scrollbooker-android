package com.example.scrollbooker.ui.myBusiness.mySchedules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.entity.booking.schedule.domain.useCase.GetSchedulesByUserIdUseCase
import com.example.scrollbooker.entity.booking.schedule.domain.useCase.UpdateSchedulesUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MySchedulesViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getSchedulesByUserIdUseCase: GetSchedulesByUserIdUseCase,
    private val updateSchedulesUseCase: UpdateSchedulesUseCase
): ViewModel() {
    private val _schedulesState = MutableStateFlow<FeatureState<List<Schedule>>>(FeatureState.Loading)
    val schedulesState: StateFlow<FeatureState<List<Schedule>>> = _schedulesState

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    init {
        loadSchedules()
    }

    private fun loadSchedules() {
        viewModelScope.launch {
            _schedulesState.value = FeatureState.Loading

            val userId = authDataStore.getUserId().firstOrNull()

            if(userId == null) {
                Timber.tag("Schedules").e("User Id not found in datastore")
                _schedulesState.value = FeatureState.Error()
                return@launch
            }

            val result = withVisibleLoading {
                getSchedulesByUserIdUseCase(userId)
            }

            _schedulesState.value = result
        }
    }

    fun updateScheduleTime(schedule: Schedule) {
        val current = _schedulesState.value

        if(current is FeatureState.Success) {
            _schedulesState.value = FeatureState.Success(
                current.data.map {
                    if(it.id == schedule.id) it.copy(
                        startTime = schedule.startTime,
                        endTime = schedule.endTime
                    ) else it
                }
            )
        }
    }

    suspend fun updateSchedules(): List<Schedule>? {
        _isSaving.value = true

        val schedules = (_schedulesState.value as? FeatureState.Success)?.data
        if (schedules.isNullOrEmpty()) return null

        val result = withVisibleLoading { updateSchedulesUseCase(schedules) }

        return result
            .onFailure { error ->
                _isSaving.value = false
                Timber.Forest.tag("Schedules").e("ERROR: on Updating Schedules $error")
            }
            .onSuccess { updated ->
                _isSaving.value = false
            }
            .getOrNull()
    }
}
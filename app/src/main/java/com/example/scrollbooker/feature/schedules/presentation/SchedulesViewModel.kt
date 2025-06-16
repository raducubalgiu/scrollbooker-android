package com.example.scrollbooker.feature.schedules.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.schedules.domain.model.Schedule
import com.example.scrollbooker.feature.schedules.domain.useCase.GetSchedulesByUserIdUseCase
import com.example.scrollbooker.feature.schedules.domain.useCase.UpdateSchedulesUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SchedulesViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getSchedulesByUserIdUseCase: GetSchedulesByUserIdUseCase,
    private val updateSchedulesUseCase: UpdateSchedulesUseCase
): ViewModel() {
    private val _schedulesState =
        MutableStateFlow<FeatureState<List<Schedule>>>(FeatureState.Loading)
    val schedulesState: StateFlow<FeatureState<List<Schedule>>> = _schedulesState

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    init {
        loadSchedules()
    }

    private fun loadSchedules() {
        viewModelScope.launch {
            _schedulesState.value = FeatureState.Loading

            val userId = authDataStore.getUserId().firstOrNull()

            if(userId == null) {
                Timber.tag("Schedules").e("User Id not found in datastore")
                FeatureState.Error()
            } else {
                _schedulesState.value = getSchedulesByUserIdUseCase(userId)
            }
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

    fun updateSchedules() {
        viewModelScope.launch {
            _isSaving.value = true

            val current = (_schedulesState.value as? FeatureState.Success)?.data ?: return@launch

            updateSchedulesUseCase(current)
                .onSuccess { updated ->
                    _schedulesState.value = FeatureState.Success(updated)
                }
                .onFailure { error ->
                    _schedulesState.value = FeatureState.Error(error)
                    Timber.Forest.tag("Schedules").e("ERROR: on Updating Schedules $error")
                }

            delay(300)
            _isSaving.value = false
        }
    }
}
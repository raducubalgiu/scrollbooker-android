package com.example.scrollbooker.feature.myBusiness.presentation.schedules
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.myBusiness.domain.model.Schedule
import com.example.scrollbooker.feature.myBusiness.domain.useCase.schedules.GetSchedulesUseCase
import com.example.scrollbooker.feature.myBusiness.domain.useCase.schedules.UpdateSchedulesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SchedulesViewModel @Inject constructor(
    private val getSchedulesUseCase: GetSchedulesUseCase,
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
            delay(300)

            getSchedulesUseCase()
                .onSuccess { result ->
                    _schedulesState.value = FeatureState.Success(result)
                }
                .onFailure { error ->
                    _schedulesState.value = FeatureState.Error(error)
                    Timber.tag("Schedules").e("ERROR: on Fetching Schedules $error")
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
                    Timber.tag("Schedules").e("ERROR: on Updating Schedules $error")
                }

            delay(500)
            _isSaving.value = false
        }
    }
}